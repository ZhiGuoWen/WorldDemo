package com.wenzhiguo.worlddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Custom mCustom = (Custom) findViewById(R.id.custom);
            InputStream open = getAssets().open("world.jpg");
            mCustom.setInput(open);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
