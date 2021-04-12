package com.example.lab7.b;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab7.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        GameView gameView = findViewById(R.id.gameView);
        gameView.start();
    }
}