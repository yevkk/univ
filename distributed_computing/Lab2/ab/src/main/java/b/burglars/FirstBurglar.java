package b.burglars;

import b.Item;
import b.PCItemQueue;

import java.util.Queue;

public class FirstBurglar extends Burglar {
    private final Queue<Item> storage;

    public FirstBurglar(Queue<Item> storage, PCItemQueue queue) {
        super(queue);
        this.storage = storage;
    }
    @Override
    public void action() {
        var item = storage.poll();
        stop = item == null;
        queue.put(item);
    }
}
