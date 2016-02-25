package org.zero.collection.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import org.zero.collection.R;
import org.zero.collection.util.ZxingUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView image = (ImageView) findViewById(R.id.imageView);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bitmap bitmap = null;
                try {
                    Bitmap  bitmap= ZxingUtil.getInstance().encodeAsBitmap(MainActivity.this, "哈哈哈");
                } catch (WriterException e) {
                    e.printStackTrace();
                }
//                image.setImageBitmap(bitmap);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZxingUtil.getInstance().decode(MainActivity.this);
            }
        });
    }
}
