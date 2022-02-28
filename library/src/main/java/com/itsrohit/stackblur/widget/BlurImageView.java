package com.itsrohit.stackblur.widget;

import android.widget.ImageView;
import android.content.Context;
import android.util.AttributeSet;
import android.content.res.TypedArray;
import com.itsrohit.stackblur.R;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.itsrohit.stackblur.StackBlur;
import android.graphics.drawable.Drawable;

public class BlurImageView extends ImageView {

	private int radius = 8;
	private boolean compress = false;
	
	private Bitmap renderingBitmap;
    private Bitmap originalBitmap;

    public BlurImageView(Context ctx) {
		super(ctx);
		init();
	}

	public BlurImageView(Context ctx, AttributeSet attrSet) {
		super(ctx, attrSet);
		fillAttrs(attrSet);
		init();
	}

	public BlurImageView(Context ctx, AttributeSet attrSet, int defStyle) {
		super(ctx, attrSet, defStyle);
		fillAttrs(attrSet);
		init();
	}

	private void fillAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BlurImageView);

        radius = array.getInt(R.styleable.BlurImageView_radius, radius);
        compress = array.getBoolean(R.styleable.BlurImageView_compress, compress);

        array.recycle();
    }
	
	private void init() {
        if (getDrawable() != null && getDrawable() instanceof BitmapDrawable) {
            originalBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        }

        setBlurIfChanged();
    }
	
	public void setRadius(int value) {
		if (radius == value) {
			return;
		}
		
		radius = value;
		setBlurIfChanged();
	}
	
	public void setCompress(boolean value) {
		compress = value;
		
		setBlurIfChanged();
	}
	
	private void setBlurIfChanged() {
        if (originalBitmap != null) {
			if (radius < 1) {
                setImageDrawable(new BitmapDrawable(originalBitmap));
            } else {
                if (renderingBitmap == null) {
                    renderingBitmap = originalBitmap;
                }

                Bitmap blurBitmap = StackBlur.blurBitmap(renderingBitmap, radius, compress);
                setImageDrawable(new BitmapDrawable(blurBitmap));
            }
		}
    }

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		init();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		init();
	}
}
