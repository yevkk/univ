package com.example.lab1.a;

public class SliderMover implements Runnable {
    private final SeekBarWrapper editTextWrapper;
    private final SeekBarWrapper.Action action;
    private Thread thread;
    private boolean stop = false;

    public SliderMover(SeekBarWrapper editTextWrapper, SeekBarWrapper.Action action, String name) {
        this.editTextWrapper = editTextWrapper;
        this.action = action;
        thread = new Thread(this, name);
    }

    @Override
    public void run() {
        while (!stop) {
            editTextWrapper.changeSelection(action);
            try {
                Thread.sleep(500 / Thread.currentThread().getPriority());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        stop = true;
    }

    public Thread getThread() {
        return thread;
    }
}
