package b;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class BMain {
    public static void main(String[] args) {
        Queue<Item> storage = new LinkedList<Item>();
        //Todo: fill storage
        var midpoint = new PCItemQueue();
        var truck = new PCItemQueue();
        var sync = new AtomicBoolean();

        var Ivanov = new Burglar(midpoint, sync) {
            private Queue<Item> storage;

            public void setStorage(Queue<Item> storage) {
                this.storage = storage;
            }

            @Override
            public void action() {
                var item = this.storage.poll();
                if (item != null) {
                    this.queue.put(item);
                } else {
                    this.sync.set(false);
                }
            }
        };
        Ivanov.setStorage(storage);

        var Petrov = new Burglar(midpoint, sync) {
            private PCItemQueue additionalQueue;

            public void setAdditionalQueue(PCItemQueue additionalQueue) {
                this.additionalQueue = additionalQueue;
            }

            @Override
            public void action() {
                this.additionalQueue.put(this.queue.get());
            }
        };
        Petrov.setAdditionalQueue(truck);

        var Nechiporuk = new Burglar(truck, sync) {
            private int sum = 0;

            @Override
            public void action() {
                sum += this.queue.get().getCost();
            }

            public int getSum() {
                return this.sum;
            }
        };


        try {
            Ivanov.getThread().join();
            Petrov.getThread().join();
            Nechiporuk.getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Nechiporuk.getSum());
    }





}
