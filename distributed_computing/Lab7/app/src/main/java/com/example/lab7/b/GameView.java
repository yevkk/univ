package com.example.lab7.b;

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
        this.points = new Point[targetsNo + 1];
        this.done = new AtomicBoolean[targetsNo + 1];
        this.semaphore = new Semaphore(1, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (points[0] != null) {
            Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.hunter, null);
            d.setBounds((int) points[0].x - Target.size, (int) points[0].y - Target.size, (int) points[0].x + Target.size, (int) points[0].y + Target.size);
            d.draw(canvas);
        }
        for (int i = 1; i <= targetsNo; i++) {
            if (points[i] != null) {
                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.target, null);
                d.setBounds((int) points[i].x - Target.size, (int) points[i].y - Target.size, (int) points[i].x + Target.size, (int) points[i].y + Target.size);
                d.draw(canvas);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        try {
//            semaphore.acquire();
//            for (int i = 0; i < targetsNo; i++) {
//                if (Math.abs(points[i].x - event.getX()) < Target.size && Math.abs(points[i].x - event.getX()) < Target.size) {
//                    done[i].set(true);
//                    done[i] = null;
//                }
//            }
//            semaphore.release();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.getHeight();
    }

    void start() {
        new Thread(() -> {
            done[0] = new AtomicBoolean(false);
            Hunter hunter = new Hunter(this, new Exchanger<>(), done[0]);
            new Thread(hunter).start();

            Target[] targets = new Target[targetsNo];
            for (int i = 0; i < targetsNo; i++) {
                done[i + 1] = new AtomicBoolean(false);
                targets[i] = new Target(this, new Exchanger<>(), done[i]);
                new Thread(targets[i]).start();
            }

            try {
                while (true) {
                    Thread.sleep(50);

                    semaphore.acquire();
                    points[0] = hunter.getExchanger().exchange(null);
                    for (int i = 0; i < targetsNo; i++) {
                        if (done[i + 1] != null) {
                            points[i + 1] = targets[i].getExchanger().exchange(null);
                        } else {
                            done[i + 1] = new AtomicBoolean(false);
                            targets[i] = new Target(this, new Exchanger<>(), done[i + 1]);
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
