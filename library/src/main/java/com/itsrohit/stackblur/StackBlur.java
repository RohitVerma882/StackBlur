package com.itsrohit.stackblur;

import android.graphics.Bitmap;
import java.util.concurrent.Callable;
import java.util.ArrayList;
import com.itsrohit.stackblur.utils.Utils;
import android.util.Log;

public class StackBlur {
	
	private static final String TAG = StackBlur.class.getSimpleName();
	
	private static native void sBlurBitmap(Bitmap bitmap, int radius);
    private static native void sBlurBitmap2(Bitmap bitmap, int radius, int threadCount, int threadIndex, int round);
	
	private static String LIB_NAME = "stackblur";
	private static boolean libLoaded = false;
	
	static {
		initLib();
	}
	
	public static synchronized void initLib() {
		if (libLoaded) {
			return;
		}
	
		System.loadLibrary(LIB_NAME);
		libLoaded = true;
	}

	public static Bitmap blurBitmap(Bitmap bitmap, int radius) {
		if (bitmap == null || radius <= 0) {
			Log.e(TAG, "blurBitmap failed! bitmap=" + (bitmap == null ? "null" : "not null") + ", radius=" + radius);
			return bitmap;
		}

		Bitmap bitmapOut = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		
        sBlurBitmap(bitmapOut, radius);
		return bitmapOut;
	}
	
	public static Bitmap blurBitmap2(Bitmap bitmap, int radius) {
		if (bitmap == null || radius <= 0) {
			Log.e(TAG, "blurBitmap2 failed! bitmap=" + (bitmap == null ? " null" : "not null") + ", radius=" + radius);
			return bitmap;
		}
		
		Bitmap bitmapOut = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		int cores = Utils.EXECUTOR_THREADS;

		ArrayList<BlurTask> horizontal = new ArrayList<BlurTask>(cores);
		ArrayList<BlurTask> vertical = new ArrayList<BlurTask>(cores);
		for (int i = 0; i < cores; i++) {
			horizontal.add(new BlurTask(bitmapOut, radius, cores, i, 1));
			vertical.add(new BlurTask(bitmapOut, radius, cores, i, 2));
		}

		try {
			Utils.EXECUTOR.invokeAll(horizontal);
		} catch (InterruptedException e) {
			return bitmapOut;
		}

		try {
			Utils.EXECUTOR.invokeAll(vertical);
		} catch (InterruptedException e) {
			return bitmapOut;
		}
		return bitmapOut;
	}
	
	private static class BlurTask implements Callable<Void> {
		private final Bitmap _bitmap;
		private final int _radius;
		private final int _totalCores;
		private final int _coreIndex;
		private final int _round;

		public BlurTask(Bitmap bitmap, int radius, int totalCores, int coreIndex, int round) {
			_bitmap = bitmap;
			_radius = radius;
			_totalCores = totalCores;
			_coreIndex = coreIndex;
			_round = round;
		}

		@Override 
		public Void call() throws Exception {
			sBlurBitmap2(_bitmap, _radius, _totalCores, _coreIndex, _round);
			return null;
		}
	}
}
