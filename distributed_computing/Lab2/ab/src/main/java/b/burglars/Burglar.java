package b.burglars;

import b.PCItemQueue;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Burglar implements Runnable{
    protected final PCItemQueue queue;
    protected final AtomicBoolean sync;
    private final Thread thread;

    public Burglar(PCItemQueue queue, AtomicBoolean sync) {
        this.queue = queue;
        this.sync = sync;
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while(sync.get()) {
            action();
        }
    }

    public abstract void action();

    public Thread getThread() {
        return thread;
    }
}
