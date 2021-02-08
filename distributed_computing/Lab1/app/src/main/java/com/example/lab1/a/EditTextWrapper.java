package com.example.lab1.a;

import android.widget.EditText;

public class EditTextWrapper {
    public enum Action {INC, DEC}

    private final EditText editText;
    private final int min;
    private final int max;

    public EditTextWrapper(EditText editText, int min, int max) {
        this.editText = editText;
        this.min = min;
        this.max = max;
    }

    public synchronized void changeSelection(Action action) {
        switch (action) {
            case INC:
                if (editText.getSelectionStart() + 1 <= max) {
                    editText.setSelection(editText.getSelectionStart() + 1);
                }
                break;
            case DEC:
                if (editText.getSelectionStart() - 1 >= min) {
                    editText.setSelection(editText.getSelectionStart() - 1);
                }
                break;
        }
    }
}
