package com.example.lab7.a;

import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicBoolean;

public class Target implements Runnable {
    private final Point location;
    private final GameView gameView;
    private final Exchanger<Point> exchanger;
    private final AtomicBoolean done;
    private static final int maxSpeed = 50;
    public static final int size = 75;


    public Target(GameView gameView, Exchanger<Point> exchanger, AtomicBoolean done) {
        this.gameView = gameView;
        this.exchanger = exchanger;
        this.done = done;

        location = new Point( Math.random() * 750, Math.random() * 400);
    }

    public Exchanger<Point> getExchanger() {
        return exchanger;
    }

    @Override
    public void run() {
        try {
            while (!done.get()) {
                double xSpeed = Math.random() * maxSpeed;
                double ySpeed = Math.random() * maxSpeed;
                location.x = (Math.random() < 0.5) ? Math.max(0,  location.x - xSpeed) : Math.min(gameView.getWidth(), location.x + xSpeed);
                location.y = (Math.random() < 0.5) ? Math.max(0,  location.y - ySpeed) : Math.min(gameView.getHeight(), location.y + ySpeed);

                exchanger.exchange(new Point(location.x, location.y));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
