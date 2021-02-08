package com.example.lab1.a;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ThreadPriorityBarListener implements OnSeekBarChangeListener {
    Thread thread;

    ThreadPriorityBarListener(Thread thread) {
        this.thread = thread;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (thread != null) {
            thread.setPriority(seekBar.getProgress() + 1);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
