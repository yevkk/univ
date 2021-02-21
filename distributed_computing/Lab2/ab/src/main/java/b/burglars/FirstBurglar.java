package b.burglars;

import b.Item;
import b.PCItemQueue;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class FirstBurglar extends Burglar {
    private final Queue<Item> storage;

    public FirstBurglar(Queue<Item> storage, PCItemQueue queue, AtomicBoolean sync) {
        super(queue, sync);
        this.storage = storage;
    }
    @Override
    public void action() {
        var item = storage.poll();
        if (item != null) {
            queue.put(item);
        } else {
            sync.set(false);
        }
    }
}
