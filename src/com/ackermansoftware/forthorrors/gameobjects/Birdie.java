package com.ackermansoftware.forthorrors.gameobjects;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;

import com.ackermansoftware.dackdroid.core.AnimationFrame;
import com.ackermansoftware.dackdroid.core.AnimationSequence;
import com.ackermansoftware.dackdroid.gameobjects.GameObject;
import com.ackermansoftware.dackdroid.gameobjects.Material;
import com.ackermansoftware.dackdroid.renderer.RenderQueue;
import com.ackermansoftware.dackdroid.renderer.TextureLibrary;
import com.ackermansoftware.forthorrors.R;

public class Birdie extends GameObject {

	private static final int MAX_FLIGHT_DIST = 500;
	private static final int TIME_TO_DESTINATION = 4000;

	private final PointF pos;
	private final PointF newPos = new PointF();
	private final PointF origPos = new PointF();

	private final AnimationSequence flyAnimation = new AnimationSequence(2);

	private long millisRemaining = 0;
	private long timeStarted;

	private float rotation;

	private enum State {
		IDLE, WAITING, FLYING
	};

	State state = State.IDLE;

	public Birdie(float x, float y, TextureLibrary textures) {
		pos = new PointF(x, y);
		setUpAnimations(textures);
	}

	private void setUpAnimations(TextureLibrary textures) {
		Bitmap b = textures.getTexture(R.drawable.standing_bird);
		flyAnimation.addFrame(new AnimationFrame(b, 0));

		b = textures.getTexture(R.drawable.flying_bird1);
		flyAnimation.addFrame(new AnimationFrame(b, 300));

		b = textures.getTexture(R.drawable.flying_bird2);
		flyAnimation.addFrame(new AnimationFrame(b, 300));

		flyAnimation.looping = true;
	}

	/*
	 * (non-Javadoc) Depending on our current state, call the appropriate
	 * method. each frame we are "thinking".
	 * 
	 * @see com.ackermansoftware.dackdroid.gameobjects.GameObject#think()
	 */
	@Override
	public void think() {
		switch (state) {
		case IDLE:
			idle();
			break;
		case WAITING:
			waiting();
			break;
		case FLYING:
			flying();
			break;
		}
	}

	/**
	 * If we are idle, randomly pick flying to waiting. Then, randomly pick a
	 * location to fly to and how fast to go.
	 */
	private void idle() {
		flyAnimation.resetAnimation();
		double decision = Math.random() * 2;
		if (decision > 1) {
			state = State.FLYING;
			newPos.x = (float) (pos.x + MAX_FLIGHT_DIST * Math.random() - MAX_FLIGHT_DIST / 2);
			newPos.y = (float) (pos.y + MAX_FLIGHT_DIST * Math.random() - MAX_FLIGHT_DIST / 2);
			origPos.x = pos.x;
			origPos.y = pos.y;
			flyAnimation.startAnimation();
		} else {
			state = State.WAITING;
		}
		timeStarted = System.nanoTime();
		millisRemaining = (long) (1000 + Math.random() * TIME_TO_DESTINATION);
	}

	/**
	 * If we are waiting, then just see if we've waiting long enough. If so, go
	 * back to idle to pick a new state.
	 */
	private void waiting() {
		long timeElapsed = (System.nanoTime() - timeStarted) / NANOS_PER_MILLI;
		if (timeElapsed > millisRemaining) {
			state = State.IDLE;
		}
	}

	/**
	 * If we are flying, move toward the destination the appropriate amount. If
	 * we are at the destination, go back to idle to pick another thing to do.
	 */
	private void flying() {
		long timeElapsed = (System.nanoTime() - timeStarted) / NANOS_PER_MILLI;
		float xDist = newPos.x - origPos.x;
		float yDist = newPos.y - origPos.y;
		rotation = (float) (Math.atan(yDist / xDist) * 180 / Math.PI);
		Log.i("rot", rotation + "");

		pos.x = origPos.x + xDist * (timeElapsed / (float) millisRemaining);
		pos.y = origPos.y + yDist * (timeElapsed / (float) millisRemaining);

		if (timeElapsed > millisRemaining) {
			state = State.IDLE;
		}
	}

	@Override
	public void render(RenderQueue renderQueue) {
		AnimationFrame currentFrame = flyAnimation.getFrame();
		Material m = new Material(currentFrame.drawable);
		m.setRotation(rotation);
		m.setNewPosition(pos);
		renderQueue.addToQueue(m);
	}
}
