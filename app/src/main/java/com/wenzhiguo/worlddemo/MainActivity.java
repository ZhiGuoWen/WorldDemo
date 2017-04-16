package com.wenzhiguo.worlddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wenzhiguo.worlddemo.view.ImageSurfaceView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            /*自定义
            Custom mCustom = (Custom) findViewById(R.id.custom);
            InputStream open = getAssets().open("world.jpg");
            mCustom.setInput(open);*/
            ImageSurfaceView viewById = (ImageSurfaceView) findViewById(R.id.image);
            InputStream open = getAssets().open("world.jpg");
            viewById.setInputStream(open);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
