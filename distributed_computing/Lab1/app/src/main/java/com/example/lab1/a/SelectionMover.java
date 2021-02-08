package com.example.lab1.a;

public class SelectionMover implements Runnable {
    private final EditTextWrapper editTextWrapper;
    private final EditTextWrapper.Action action;

    SelectionMover(EditTextWrapper editTextWrapper, EditTextWrapper.Action action) {
        this.editTextWrapper = editTextWrapper;
        this.action = action;
    }

    @Override
    public void run() {
        while (true) {
            editTextWrapper.changeSelection(action);
            try {
                Thread.sleep(500 / Thread.currentThread().getPriority());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
