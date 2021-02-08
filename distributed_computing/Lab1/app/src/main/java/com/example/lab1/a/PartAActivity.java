package com.example.lab1.a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.example.lab1.R;
import com.example.lab1.b.PartBActivity;

public class PartAActivity extends AppCompatActivity {
    private SliderMover sm1, sm2;

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

        SeekBarWrapper seekBarWrapper = new SeekBarWrapper(seekBar, seekBar.getMax() / 10, seekBar.getMax() * 9 / 10);
        SliderMover sm1 = new SliderMover(seekBarWrapper, SeekBarWrapper.Action.DEC, "th1-min");
        SliderMover sm2 = new SliderMover(seekBarWrapper, SeekBarWrapper.Action.INC, "th2-max");

        SeekBar th1PriorityBar = findViewById(R.id.th1PriorityBar);
        th1PriorityBar.setEnabled(true);
        th1PriorityBar.setOnSeekBarChangeListener(new ThreadPriorityBarListener(sm1));

        SeekBar th2PriorityBar = findViewById(R.id.th2PriorityBar);
        th2PriorityBar.setEnabled(true);
        th2PriorityBar.setOnSeekBarChangeListener(new ThreadPriorityBarListener(sm2));

        sm1.getThread().setPriority(((SeekBar) findViewById(R.id.th1PriorityBar)).getProgress() + 1);
        sm2.getThread().setPriority(((SeekBar) findViewById(R.id.th2PriorityBar)).getProgress() + 1);

        sm1.getThread().setDaemon(true);
        sm2.getThread().setDaemon(true);

        sm1.start();
        sm2.start();
    }

    public void onGotoBBtnClick(View view) {
        if (sm1 != null) {
            sm1.stop();
        }
        if (sm2 != null) {
            sm2.stop();
        }

        Intent intent = new Intent(this, PartBActivity.class);
        startActivity(intent);
    }
}