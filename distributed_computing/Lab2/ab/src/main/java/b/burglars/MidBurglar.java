package b.burglars;

import b.PCItemQueue;

import java.util.concurrent.atomic.AtomicBoolean;

public class MidBurglar extends Burglar {
    private PCItemQueue additionalQueue;

    public MidBurglar(PCItemQueue queue, PCItemQueue additionalQueue) {
        super(queue);
        this.additionalQueue = additionalQueue;
    }

    @Override
    public void action() {
        var item = queue.get();
        stop = item == null;
        additionalQueue.put(item);
    }
}
