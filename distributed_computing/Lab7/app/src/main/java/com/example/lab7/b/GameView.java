package com.example.lab7.b;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.example.lab7.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameView extends View {
    private final Point[] points;
    private final int targetsNo = 3;
    private final AtomicBoolean[] done;
    private final Semaphore semaphore;
    private final List<Point> bullets;
    private final Paint paint;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.points = new Point[targetsNo + 1];
        this.done = new AtomicBoolean[targetsNo + 1];
        this.semaphore = new Semaphore(1, true);
        this.bullets = new ArrayList<>();

        this.paint = new Paint();
        paint.setColor(getResources().getColor(R.color.bulletColor));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Point bullet : bullets) {
            canvas.drawCircle((float) bullet.x, (float) bullet.y, 15, paint);
        }
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
        try {
            semaphore.acquire();
            bullets.add(new Point(points[0].x, points[0].y));
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return super.onTouchEvent(event);
    }

    void start() {
        new Thread(() -> {
            done[0] = new AtomicBoolean(false);
            Hunter hunter = new Hunter(this, new Exchanger<>(), done[0]);
            new Thread(hunter).start();

            Target[] targets = new Target[targetsNo];
            for (int i = 0; i < targetsNo; i++) {
                done[i + 1] = new AtomicBoolean(false);
                targets[i] = new Target(this, new Exchanger<>(), done[i + 1]);
                new Thread(targets[i]).start();
            }

            try {
                while (true) {
                    Thread.sleep(50);

                    semaphore.acquire();
                    int k = 0;
                    while (k < bullets.size()) {
                        boolean del = false;
                        Point bullet = bullets.get(k);
                        bullet.y -= 10;
                        for (int j = 1; j <= targetsNo; j++) {
                            if (points[j] != null) {
                                if (Math.abs(points[j].x - bullet.x) < Target.size && Math.abs(points[j].y - bullet.y) < Target.size) {
                                    targets[j - 1].getDone().set(true);
                                    done[j] = null;
                                    del = true;
                                    break;
                                }
                            }
                        }
                        if (bullet.y < 0) {
                            del = true;
                        }
                        if (del) {
                            bullets.remove(k);
                        } else {
                            k++;
                        }
                    }

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
