package com.example.lab7.a;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.example.lab7.R;

import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameView extends View {
    private final Point[] points;
    private final int targetsNo = 3;
    private final AtomicBoolean[] done;
    private final Semaphore semaphore;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.points = new Point[targetsNo];
        this.done = new AtomicBoolean[targetsNo];
        this.semaphore = new Semaphore(1, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < targetsNo; i++) {
            if (points[i] != null) {
                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.target, null);
                d.setBounds((int) points[i].x - Target.size, (int) points[i].y - Target.size, (int) points[i].x + Target.size, (int) points[i].y + Target.size);
                d.draw(canvas);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            semaphore.acquire();
            for (int i = 0; i < targetsNo; i++) {
                if (Math.abs(points[i].x - event.getX()) < Target.size && Math.abs(points[i].y - event.getY()) < Target.size) {
                    done[i].set(true);
                    done[i] = null;
                }
            }
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return super.onTouchEvent(event);
    }

    void start() {
        new Thread(() -> {
            Target[] targets = new Target[targetsNo];
            for (int i = 0; i < targetsNo; i++) {
                done[i] = new AtomicBoolean(false);
                targets[i] = new Target(this, new Exchanger<>(), done[i]);
                new Thread(targets[i]).start();
            }

            try {
                while (true) {
                    Thread.sleep(50);

                    semaphore.acquire();
                    for (int i = 0; i < targetsNo; i++) {
                        if (done[i] != null) {
                            points[i] = targets[i].getExchanger().exchange(null);
                        } else {
                            done[i] = new AtomicBoolean(false);
                            targets[i] = new Target(this, new Exchanger<>(), done[i]);
                            new Thread(targets[i]).start();
                        }
                    }
                    semaphore.release();

                    invalidate();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
