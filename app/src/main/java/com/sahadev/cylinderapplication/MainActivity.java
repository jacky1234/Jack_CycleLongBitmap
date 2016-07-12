package com.sahadev.cylinderapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sahadev.view.CylinderImageView;

public class MainActivity extends AppCompatActivity {
    private CylinderImageView cylinderImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cylinderImageView = (CylinderImageView) findViewById(R.id.cylinderImageView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        cylinderImageView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        cylinderImageView.pause();
    }
}
