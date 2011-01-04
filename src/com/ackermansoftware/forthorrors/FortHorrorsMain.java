package com.ackermansoftware.forthorrors;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ackermansoftware.dackdroid.core.CameraSystem;
import com.ackermansoftware.dackdroid.core.EngineSurfaceView;
import com.ackermansoftware.dackdroid.core.GameEngine;
import com.ackermansoftware.dackdroid.core.GameEngineSettings;
import com.ackermansoftware.dackdroid.core.GameSystem;
import com.ackermansoftware.dackdroid.core.World;
import com.ackermansoftware.dackdroid.gameobjects.GameObject;
import com.ackermansoftware.forthorrors.gameobjects.Birdie;
import com.ackermansoftware.forthorrors.levels.LevelFactory;

public class FortHorrorsMain extends Activity {

	private GameEngine gameEngine;
	private CameraSystem camera;
	private World world;

	private PointF oldCam = new PointF();
	private final PointF oldTouch = new PointF();

	private boolean movingCamera = false;
	private TextView statusText;
	private TextView fps;
	private GameEngine.Stats renderStats;
	private GameEngine.Stats logicStats;

	boolean fpsToggle = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title);

		// Add game view to screen.
		EngineSurfaceView gameView = (EngineSurfaceView) findViewById(R.id.game_view);
		GameEngineSettings settings = new GameEngineSettings();
		settings.rendererMaximumFps = 20;
		settings.gameLogicMaximumFps = 20;
		gameView.init(settings);

		GameSystem system = gameView.getGameSystem();

		// Get camera and world objects for use later.
		camera = system.getCameraSystem();
		camera.setCameraPosition(new PointF(150f, 50f));
		world = system.getWorld();


		// Get Status Text
		statusText = (TextView) findViewById(R.id.status);

		// Track FPS
		gameEngine = gameView.getGameEngine();
		renderStats = gameEngine.rendererStats;
		logicStats = gameEngine.logicStats;
		fps = (TextView) findViewById(R.id.fps);

		// Initialize world with level
		LevelFactory level = new LevelFactory(getResources());
		level.create(R.raw.level_1, world);

		createBirdies();
	}

	private void createBirdies() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				GameObject b = new Birdie(200f + (float) Math.random() * 50f * i, 200f
						+ (float) Math.random() * 50f * j);
				world.addGameObject(b);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			tapped(event);
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			return moving(event);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			fingerUp();
			return true;
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	private void tapped(MotionEvent event) {
		oldCam = camera.getCameraPosition();
		oldTouch.x = event.getX();
		oldTouch.y = event.getY();
		movingCamera = true;
	}

	private boolean moving(MotionEvent event) {
		if (movingCamera) {
			float newX = oldCam.x - (event.getX() - oldTouch.x);
			float newY = oldCam.y - (event.getY() - oldTouch.y);
			PointF newCameraPosition = new PointF(newX, newY);
			camera.setCameraPosition(newCameraPosition);
			statusText.setText(String.format("Camera: %.2f, %.2f", newCameraPosition.x,
					newCameraPosition.y));
			fps.setText(String.format("render:%.2f fps\nlogic:  %.2f fps", renderStats
					.getAverageFPS(), logicStats.getAverageFPS()));
			return true;
		}
		return false;
	}

	private void fingerUp() {
		movingCamera = false;
	}
}