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

	private ImageView blurImageView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// blur sample image
		blurImageView = findViewById(R.id.img);
        blurImageView.post(new Runnable() {
				@Override
				public void run() {
					loadBluredImage();
				}
			});
    }
	
	private void loadBluredImage() {
		// StackBlur#blurBitmap2 method suports multi thered and faster
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_img);
		blurImageView.setImageBitmap(StackBlur.blurBitmap2(bitmap, 40));
	}
}
