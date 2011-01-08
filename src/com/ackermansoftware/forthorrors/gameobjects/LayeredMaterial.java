package com.ackermansoftware.forthorrors.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Canvas.EdgeType;

import com.ackermansoftware.dackdroid.renderer.Renderable;

public class LayeredMaterial implements Renderable {
	private Bitmap[] textures;

	private PointF coordinates;
	private float rotation;

	public void setCoordinates(PointF coordinates) {
		this.coordinates.x = coordinates.x;
		this.coordinates.y = coordinates.y;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void setResources(Bitmap[] textures) {
		this.textures = textures;
	}

	@Override
	public void render(Canvas c) {
		float x = coordinates.x;
		float y = coordinates.y;
		float w = textures[0].getWidth();
		float h = textures[0].getHeight();
		if (!c.quickReject(x, y, x + w, y + h, EdgeType.BW)) {
			if (rotation != 0) {
				// If we have rotation, we need to translate and rotate the
				// matrix.
				c.save();
				c.translate(x, y);
				c.rotate(rotation);
				for (Bitmap texture : textures) {
					c.drawBitmap(texture, 0, 0, null);
				}
				c.restore();
			} else {
				// If we don't have rotation, we can draw directly.
				for (Bitmap texture : textures) {
					c.drawBitmap(texture, x, y, null);
				}
			}

		}
	}

}
