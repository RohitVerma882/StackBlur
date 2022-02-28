package com.itsrohit.stackblursample.ui;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.itsrohit.stackblursample.R;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.itsrohit.stackblur.StackBlur;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// blur sample image
		final ImageView blurImg = findViewById(R.id.img);
        blurImg.post(new Runnable() {
				@Override
				public void run() {
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_img);
					blurImg.setImageBitmap(StackBlur.blurBitmap(bitmap, true));
				}
			});
    }
}
