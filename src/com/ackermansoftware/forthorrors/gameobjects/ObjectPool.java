package com.ackermansoftware.forthorrors.gameobjects;

import android.util.Log;

public class ObjectPool<T> {

	private final T[] pool;
	private int pos;

	public ObjectPool(T[] pool) {
		this.pool = pool;
		pos = 0;
	}

	public synchronized T get() {
		if (pos < pool.length) {
			T obj = pool[pos];
			pool[pos] = null;
			pos++;
			return obj;
		} else {
			throw new ExhaustedPoolException("Pool has no more objects.");
		}
	}

	public synchronized void put(T obj) {
		if (pos > 0) {
			pos--;
			pool[pos] = obj;
		}else {
			Log.w("ObjectPool", "Tried to put object into full pool.");
		}
	}

}
