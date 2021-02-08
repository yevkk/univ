package com.example.lab1.b;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.lab1.R;

public class PartBActivity extends AppCompatActivity {
    private int semaphore;
    Thread th1, th2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.stopTh1Btn).setEnabled(false);
        findViewById(R.id.stopTh2Btn).setEnabled(false);

        semaphore = 1;

        setContentView(R.layout.activity_part_b);
    }

    public void onStartTh1BtnClick(View view) {

    }

    public void onStartTh2BtnClick(View view) {

    }

    public void onStopTh1BtnClick(View view) {
        
    }

    public void onStopTh2BtnClick(View view) {

    }
}