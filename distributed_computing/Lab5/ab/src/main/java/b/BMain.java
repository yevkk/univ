package b;

import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class BMain {
    public static void main(String[] args) throws InterruptedException {
        var done = new AtomicBoolean(false);

        var handlers = new StringHandler[]{
                new StringHandler(15, done),
                new StringHandler(15, done),
                new StringHandler(15, done),
                new StringHandler(15, done)
        };

        var threads = Arrays.stream(handlers).map(Thread::new).toArray(Thread[]::new);

        var barrier = new CyclicBarrier(4, () -> {
            try {
                Thread.sleep(200);
                System.out.println();

                var counters = Arrays.stream(handlers).mapToInt(StringHandler::getCounter).toArray();
                for (int i = 0; i < handlers.length; i++) {
                    System.out.printf("Handler-%d: %s, %d\n", i, handlers[i].getString(), counters[i]);
                }

                if (((counters[0] == counters[1]) || (counters[2] == counters[3])) && ((counters[0] == counters[2]) || (counters[1] == counters[3]))) {
                    done.set(true);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        for (var handler : handlers) {
            handler.setBarrier(barrier);
        }

        for (var thread : threads) {
            thread.start();
        }

        for (var thread : threads) {
            thread.join();
        }
    }
}
