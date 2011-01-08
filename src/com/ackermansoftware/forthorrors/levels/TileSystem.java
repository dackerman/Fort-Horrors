package com.ackermansoftware.forthorrors.levels;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.ackermansoftware.dackdroid.R;
import com.ackermansoftware.dackdroid.gameobjects.GameObject;
import com.ackermansoftware.dackdroid.renderer.RenderQueue;
import com.ackermansoftware.dackdroid.renderer.Renderable;
import com.ackermansoftware.dackdroid.renderer.TextureLibrary;

public class TileSystem extends GameObject {
	public static final int tileScale = 50;

	public static final int maxCols = 100;
	public static final int maxRows = 100;

	private static final int[] assets = new int[] { R.drawable.tile_grass, R.drawable.tile_metal,
		R.drawable.tile_metal_topleft, R.drawable.tile_metal_topright,
		R.drawable.tile_metal_bottomright, R.drawable.tile_metal_bottomleft,
		R.drawable.tile_metal_wall };
	private static final Bitmap[] assetCache = new Bitmap[assets.length];

	private final int[][] tiles = new int[maxCols][maxRows];

	public TileSystem(TextureLibrary textures) {
		init();
		// Fill in bitmaps
		for(int i=0;i<assets.length;i++) {
			assetCache[i] = textures.getTexture(assets[i]);
		}
	}

	private void init() {
		// Initialize all tiles to -1 texture, meaning do not render.
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				tiles[x][y] = -1;
			}
		}
	}

	public void addTile(int x, int y, int intValue) {
		if (x < maxCols && y < maxRows) {
			tiles[x][y] = intValue;
		}
	}

	@Override
	public void render(RenderQueue renderQueue) {
		int[][] tilesCopy = tiles.clone();
		RenderableTiles tilesToRender = new RenderableTiles(tilesCopy);
		renderQueue.addToQueue(tilesToRender);
	}

	class RenderableTiles implements Renderable {
		private final int[][] tiles;

		public RenderableTiles(int[][] tiles) {
			this.tiles = tiles;
		}

		@Override
		public void render(Canvas c) {
			// Clamp all tile values to numbers within the array's range. That
			// way, we don't have to do a bunch of checks in the for loops.
			Rect b = c.getClipBounds();
			int tileLeft = clamp(b.left / tileScale, 0, maxCols - 1);
			int tileRight = clamp(b.right / tileScale + 1, 0, maxCols - 1);
			int tileTop = clamp(b.top / tileScale, 0, maxRows - 1);
			int tileBottom = clamp(b.bottom / tileScale + 1, 0, maxRows - 1);

			Bitmap tileBmp;
			for (int x = tileLeft; x < tileRight; x++) {
				for (int y = tileTop; y < tileBottom; y++) {
					if (tiles[x][y] != -1) {
						tileBmp = assetCache[tiles[x][y]];
						if (tiles[x][y] == 6) {
							c.drawBitmap(assetCache[1], x * tileScale, y * tileScale, null);
						}
						c.drawBitmap(tileBmp, x * tileScale, y * tileScale, null);
					}
				}
			}

		}

		private int clamp(int value, int min, int max) {
			return Math.max(Math.min(value, max), min);
		}

	}

}
