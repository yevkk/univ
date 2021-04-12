package com.example.lab7.a;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lab7.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GameView gameView = findViewById(R.id.gameView);
        gameView.start();
    }
}