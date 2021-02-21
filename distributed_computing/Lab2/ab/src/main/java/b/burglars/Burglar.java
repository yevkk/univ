package b.burglars;

import b.PCItemQueue;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Burglar implements Runnable{
    protected final PCItemQueue queue;
    protected boolean stop = false;
    private final Thread thread;

    public Burglar(PCItemQueue queue) {
        this.queue = queue;
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while(!stop) {
            action();
        }
    }

    public abstract void action();

    public Thread getThread() {
        return thread;
    }
}
