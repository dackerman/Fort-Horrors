package com.ackermansoftware.forthorrors.levels;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;
import android.util.Log;

import com.ackermansoftware.dackdroid.core.World;

public class LevelFactory {
	private final Resources res;

	public LevelFactory(Resources res) {
		this.res = res;
	}

	/**
	 * Read a level file with resource ID of levelId, and initialize it into the
	 * world provided.
	 * 
	 * @param levelId
	 *            Resource Id in which to read level data from.
	 * @param world
	 *            World to populate level data with.
	 */
	public void create(int levelId, World world) {
		String levelString = getLevelData(levelId, 2000);
		Log.i("LevelFactory", String.format("Processed %s bytes of data.", levelString.length()));
		String[] rows = levelString.split("\n");

		TileSystem tiles = new TileSystem();
		for (int y = 0; y < rows.length; y++) {
			for (int x = 0; x < rows[y].length(); x++) {
				String c = rows[y].substring(x, x + 1);
				int intValue = getTileValue(c);
				// Skip any tiles out of range.
				if (intValue < 0) {
					continue;
				}
				Log.i("LevelFactory", String.format("Creating Tile at %d, %d with asset %d", x, y,
						intValue));
				tiles.addTile(x, y, intValue);
			}
		}
		world.addGameObject(tiles);
	}

	private int getTileValue(String c) {
		int intValue;
		try {
			intValue = Integer.parseInt(c);
			return intValue;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Does the dirty work of reading in all the bytes of level data. It returns
	 * a string representing the level, including newlines.
	 * 
	 * @param levelId
	 *            Resource Id to read from
	 * @param outBytes
	 *            Bytes to populate with data.
	 * @return String representing the level file.
	 */
	private String getLevelData(int levelId, int maxSize) {
		byte[] bytes = new byte[maxSize];
		InputStream levelStream = res.openRawResource(levelId);
		int offset = 0;
		int len = bytes.length;
		int processed = 0;
		int result = 0;
		// Commence super-ugly java code to read bytes from file. Throw
		// exceptions if shit hits the fan.
		try {
			do {
				result = levelStream.read(bytes, offset, len - offset);
				offset += processed;
				if (result != -1) {
					processed += result;
				}
			} while (result != -1 && processed < len);
		} catch (IOException e) {
			throw new RuntimeException("Level data corrupted!", e);
		} finally {
			try {
				levelStream.close();
			} catch (IOException e) {
				throw new RuntimeException("Failed to close resource!");
			}
		}
		return new String(bytes, 0, processed);
	}
}
