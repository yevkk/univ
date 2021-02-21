package b.burglars;

import b.PCItemQueue;

import java.util.concurrent.atomic.AtomicBoolean;

public class LastBurglar extends Burglar {
    private int sum = 0;

    public LastBurglar(PCItemQueue queue, AtomicBoolean sync) {
        super(queue, sync);
    }

    @Override
    public void action() {
        sum += this.queue.get().getCost();
    }

    public int getSum() {
        return this.sum;
    }
}
