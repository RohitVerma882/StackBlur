package com.itsrohit.stackblur;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Canvas;

public class StackBlur {

    private static final String LIB_NAME = "stackblur";
    private static volatile boolean libLoaded = false;
    
	private static final int DEFUALT_RADIUS = 8;
	
    static {
        // init lib, if not inited
        initLib();
    }
	
    public static void initLib() {
        if(libLoaded) {
            return;
        }
        
        try {
            System.loadLibrary(LIB_NAME);
            libLoaded = true;
        } catch(Error e) {
            throw e;
        }
    }

	private static native void stackBlurBitmap(Bitmap bitmap, int radius);
    
	public static Bitmap blurBitmap(Bitmap bitmap) {
		return blurBitmap(bitmap, DEFUALT_RADIUS, false);
	}
	
	public static Bitmap blurBitmap(Bitmap bitmap, int radius) {
		return blurBitmap(bitmap, radius, false);
	}
	
	public static Bitmap blurBitmap(Bitmap bitmap, boolean compress) {
		return blurBitmap(bitmap, DEFUALT_RADIUS, compress);
	}
	
    public static Bitmap blurBitmap(Bitmap bitmap, int radius, boolean compress) {
		if (bitmap == null || radius <= 0) {
			return bitmap;
		}
		
        Bitmap finalBitmap;
        if (compress) {
            Bitmap compresed = getCompresedBitmap(bitmap);
			stackBlurBitmap(compresed, radius);
			finalBitmap = compresed;
        } else {
            stackBlurBitmap(bitmap, radius);
            finalBitmap = bitmap;
        }
		return finalBitmap;
    }
    
	private static Bitmap getCompresedBitmap(Bitmap bitmap) {
		Bitmap compressedBitmap;
		if (bitmap.getHeight() > bitmap.getWidth()) {
			compressedBitmap = Bitmap.createBitmap((int)
												   Math.round(450f * bitmap.getWidth() / bitmap.getHeight()),
												   450,
												   Bitmap.Config.ARGB_8888
												   );
		} else {
			compressedBitmap = Bitmap.createBitmap(
				450,
				(int) Math.round(450f * bitmap.getHeight() / bitmap.getWidth()),
				Bitmap.Config.ARGB_8888
			);
		}
		Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		Rect rect = new Rect(0, 0, compressedBitmap.getWidth(), compressedBitmap.getHeight());
		new Canvas(compressedBitmap).drawBitmap(bitmap, null, rect, paint);
		return compressedBitmap;
	}  
}
