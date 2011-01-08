package com.ackermansoftware.forthorrors.gameobjects;

import android.graphics.Bitmap;

import com.ackermansoftware.dackdroid.R;
import com.ackermansoftware.dackdroid.gameobjects.GameObject;
import com.ackermansoftware.dackdroid.renderer.RenderQueue;
import com.ackermansoftware.dackdroid.renderer.TextureLibrary;

public class Player extends GameObject {
	private final Bitmap[] standingTextures = new Bitmap[4];
	private final Bitmap[] walkingTextures = new Bitmap[4];

	public Player(TextureLibrary t) {
		Bitmap b = t.getTexture(R.drawable.player_shadow);
		standingTextures[0] = b;
		walkingTextures[0] = b;

		b = t.getTexture(R.drawable.player_standing);
		standingTextures[1] = b;

		b = t.getTexture(R.drawable.player_walking);
		walkingTextures[1] = b;

		b = t.getTexture(R.drawable.player_head);
		standingTextures[2] = b;
		walkingTextures[2] = b;

		b = t.getTexture(R.drawable.player_hair);
		standingTextures[3] = b;
		walkingTextures[3] = b;
	}

	@Override
	public void think() {

	}

	@Override
	public void updateState() {
	}

	@Override
	public void render(RenderQueue renderQueue) {
		LayeredMaterial lm = new LayeredMaterial();

		renderQueue.addToQueue(lm);
	}
}
