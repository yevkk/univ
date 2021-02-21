package b.burglars;

import b.PCItemQueue;

import java.util.concurrent.atomic.AtomicBoolean;

public class LastBurglar extends Burglar {
    private int sum = 0;

    public LastBurglar(PCItemQueue queue) {
        super(queue);
    }

    @Override
    public void action() {
        var item = this.queue.get();
        if (item == null) {
            stop = true;
        } else {
            sum += item.getCost();
        }
    }

    public int getSum() {
        return this.sum;
    }
}
