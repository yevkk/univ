package com.example.lab1.a;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ThreadPriorityBarListener implements OnSeekBarChangeListener {
    private final SliderMover sliderMover;

    public ThreadPriorityBarListener(SliderMover sliderMover) {
        this.sliderMover = sliderMover;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (sliderMover.getThread() != null) {
            sliderMover.getThread().setPriority(seekBar.getProgress() + 1);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
