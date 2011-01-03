package com.ackermansoftware.forthorrors;

import android.graphics.Point;
import android.graphics.PointF;

import com.ackermansoftware.dackdroid.R;
import com.ackermansoftware.dackdroid.gameobjects.GameObject;
import com.ackermansoftware.dackdroid.gameobjects.Material;
import com.ackermansoftware.dackdroid.renderer.RenderQueue;

public class Tile extends GameObject {

	private static final int tileScale = 50;

	private Material material;
	private final Point tilePosition;

	private static final int[] assets = new int[] { R.drawable.tile_grass, R.drawable.tile_metal };
	private int asset = 0;

	public Tile(Point tilePosition, int assetId) {
		this.tilePosition = tilePosition;
		asset = assets[assetId];
	}

	public Tile(int x, int y, int assetId) {
		this(new Point(x, y), assetId);
	}

	private PointF getPixelPosition() {
		float x = tilePosition.x * tileScale;
		float y = tilePosition.y * tileScale;
		return new PointF(x, y);
	}

	@Override
	public void think() {
	}

	@Override
	public void render(RenderQueue renderQueue) {
		material = new Material(getPixelPosition(), asset);
		renderQueue.addToQueue(material);
	}
}
