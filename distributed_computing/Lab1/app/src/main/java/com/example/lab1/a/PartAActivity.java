package com.example.lab1.a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.example.lab1.R;
import com.example.lab1.b.PartBActivity;

public class PartAActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.th1PriorityBar).setEnabled(false);
        findViewById(R.id.th2PriorityBar).setEnabled(false);
    }

    public void onStartBtnCLick(View view) {
        view.setEnabled(false);

        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(50);
        seekBar.setOnSeekBarChangeListener(null);



        SeekBarWrapper seekBarWrapper = new SeekBarWrapper(seekBar, seekBar.getMax() / 10, seekBar.getMax() * 9 / 10);
        SliderMover th1 = new SliderMover(seekBarWrapper, SeekBarWrapper.Action.DEC, "th1-min");
        SliderMover th2 = new SliderMover(seekBarWrapper, SeekBarWrapper.Action.INC, "th2-max");

        SeekBar th1PriorityBar = findViewById(R.id.th1PriorityBar);
        th1PriorityBar.setEnabled(true);
        th1PriorityBar.setOnSeekBarChangeListener(new ThreadPriorityBarListener(th1.getThread()));

        SeekBar th2PriorityBar = findViewById(R.id.th2PriorityBar);
        th2PriorityBar.setEnabled(true);
        th2PriorityBar.setOnSeekBarChangeListener(new ThreadPriorityBarListener(th2.getThread()));

        th1.getThread().setPriority(((SeekBar) findViewById(R.id.th1PriorityBar)).getProgress() + 1);
        th2.getThread().setPriority(((SeekBar) findViewById(R.id.th2PriorityBar)).getProgress() + 1);

        th1.getThread().setDaemon(true);
        th2.getThread().setDaemon(true);

        th1.getThread().start();
        th2.getThread().start();
    }

    public void onGotoBBtnClick(View view) {
        Intent intent = new Intent(this, PartBActivity.class);
        startActivity(intent);
    }
}