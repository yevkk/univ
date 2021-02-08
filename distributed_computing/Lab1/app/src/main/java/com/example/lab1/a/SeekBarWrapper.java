package com.example.lab1.a;

import android.widget.SeekBar;

public class SeekBarWrapper {
    public enum Action {INC, DEC}

    private final SeekBar seekBar;
    private final int min;
    private final int max;

    public SeekBarWrapper(SeekBar seekBar, int min, int max) {
        this.seekBar = seekBar;
        this.min = min;
        this.max = max;
    }

    public synchronized void changeSelection(Action action) {
        switch (action) {
            case INC:
                if (seekBar.getProgress() + 1 <= max) {
                    seekBar.setProgress(seekBar.getProgress() + 1);
                }
                break;
            case DEC:
                if (seekBar.getProgress() - 1 >= min) {
                    seekBar.setProgress(seekBar.getProgress() - 1);
                }
                break;
        }
    }
}
