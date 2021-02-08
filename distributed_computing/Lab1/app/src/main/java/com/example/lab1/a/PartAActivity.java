package com.example.lab1.a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.lab1.R;
import com.example.lab1.b.PartBActivity;

public class PartAActivity extends AppCompatActivity {
    Thread th1, th2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.th1PriorityBar).setEnabled(false);
        findViewById(R.id.th2PriorityBar).setEnabled(false);
    }

    public void onStartBtnCLick(View view) {
        view.setEnabled(false);

        EditText editText = findViewById(R.id.editText);
        editText.requestFocus();
        editText.setSelection(50);

        EditTextWrapper editTextWrapper = new EditTextWrapper(editText, 10, 90);
        th1 = new Thread(new SelectionMover(editTextWrapper, EditTextWrapper.Action.DEC), "th1-min");
        th2 = new Thread(new SelectionMover(editTextWrapper, EditTextWrapper.Action.INC), "th2-max");

        SeekBar th1PriorityBar = findViewById(R.id.th1PriorityBar);
        th1PriorityBar.setEnabled(true);
        th1PriorityBar.setOnSeekBarChangeListener(new ThreadPriorityBarListener(th1));

        SeekBar th2PriorityBar = findViewById(R.id.th2PriorityBar);
        th2PriorityBar.setEnabled(true);
        th2PriorityBar.setOnSeekBarChangeListener(new ThreadPriorityBarListener(th2));

        th1.setPriority(((SeekBar) findViewById(R.id.th1PriorityBar)).getProgress() + 1);
        th2.setPriority(((SeekBar) findViewById(R.id.th2PriorityBar)).getProgress() + 1);

        th1.start();
        th2.start();
    }

    public void onGotoBBtnClick(View view) {
        Intent intent = new Intent(this, PartBActivity.class);
        startActivity(intent);
    }
}