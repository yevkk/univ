package com.example.lab7.b;

import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicBoolean;

public class Hunter implements Runnable {
    protected final Point location;
    protected final GameView gameView;
    protected final Exchanger<Point> exchanger;
    protected final AtomicBoolean done;
    protected static final int speed = 20;
    public static final int size = 75;
    private int dest;

    public Hunter(GameView gameView, Exchanger<Point> exchanger, AtomicBoolean done) {
        this.gameView = gameView;
        this.exchanger = exchanger;
        this.done = done;

        dest = 1;
        location = new Point( 0, 850);
    }

    public Exchanger<Point> getExchanger() {
        return exchanger;
    }

    @Override
    public void run() {
        try {
            while (!done.get()) {
                location.x = (dest > 0) ? location.x + speed : location.x - speed;
                if (location.x > gameView.getWidth()) {
                    dest = -1;
                } else if (location.x < 0) {
                    dest = 1;
                }

                exchanger.exchange(new Point(location.x, location.y));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
