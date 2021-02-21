package b.burglars;

import b.PCItemQueue;

import java.util.concurrent.atomic.AtomicBoolean;

public class MidBurglar extends Burglar {
    private PCItemQueue additionalQueue;

    public MidBurglar(PCItemQueue queue, PCItemQueue additionalQueue, AtomicBoolean sync) {
        super(queue, sync);
        this.additionalQueue = additionalQueue;
    }

    @Override
    public void action() {
        additionalQueue.put(queue.get());
    }
}
