package com.example.lab1.b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.example.lab1.R;
import com.example.lab1.a.PartAActivity;
import com.example.lab1.a.SeekBarWrapper;
import com.example.lab1.a.SliderMover;

import java.util.concurrent.atomic.AtomicInteger;

public class PartBActivity extends AppCompatActivity {
    private final AtomicInteger semaphore = new AtomicInteger();
    private SliderMover sm1, sm2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_b);

        findViewById(R.id.stopTh1Btn).setEnabled(false);
        findViewById(R.id.stopTh2Btn).setEnabled(false);
        findViewById(R.id.errorMessage).setVisibility(View.INVISIBLE);

        semaphore.set(1);

        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(50);
    }

    public void onStartTh1BtnClick(View view) {
        if (semaphore.compareAndSet(1, 0)) {

            SeekBar seekBar = findViewById(R.id.seekBar);
            SeekBarWrapper seekBarWrapper = new SeekBarWrapper(seekBar, seekBar.getMax() / 10, seekBar.getMax() * 9 / 10);
            sm1 = new SliderMover(seekBarWrapper, SeekBarWrapper.Action.DEC, "th1-min");

            sm1.start();
            sm1.getThread().setPriority(Thread.MIN_PRIORITY);

            view.setEnabled(false);
            findViewById(R.id.stopTh1Btn).setEnabled(true);
        } else {
            showErrorMessage();
        }
    }

    public void onStartTh2BtnClick(View view) {
        if (semaphore.compareAndSet(1, 0)) {

            SeekBar seekBar = findViewById(R.id.seekBar);
            SeekBarWrapper seekBarWrapper = new SeekBarWrapper(seekBar, seekBar.getMax() / 10, seekBar.getMax() * 9 / 10);
            sm2 = new SliderMover(seekBarWrapper, SeekBarWrapper.Action.INC, "th2-max");

            sm2.start();
            sm2.getThread().setPriority(Thread.MAX_PRIORITY);

            view.setEnabled(false);
            findViewById(R.id.stopTh2Btn).setEnabled(true);
        } else {
            showErrorMessage();
        }
    }

    public void onStopTh1BtnClick(View view) {
        view.setEnabled(false);

        sm1.stop();
        semaphore.set(1);
        findViewById(R.id.startTh1Btn).setEnabled(true);
    }

    public void onStopTh2BtnClick(View view) {
        view.setEnabled(false);

        sm2.stop();
        semaphore.set(1);
        findViewById(R.id.startTh2Btn).setEnabled(true);
    }

    public void showErrorMessage() {
        findViewById(R.id.errorMessage).setVisibility(View.VISIBLE);
        (new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            findViewById(R.id.errorMessage).setVisibility(View.INVISIBLE);
        })).start();
    }

    public void onGotoABtnClick(View view) {
        if (sm1 != null) {
            sm1.stop();
        }
        if (sm2 != null) {
            sm2.stop();
        }

        Intent intent = new Intent(this, PartAActivity.class);
        startActivity(intent);
    }
}
