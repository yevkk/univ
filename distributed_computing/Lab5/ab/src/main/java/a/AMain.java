package a;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class AMain {
    public static void main(String[] args) throws InterruptedException {
        var done = new AtomicBoolean(false);
        var units = new UnitHandler[]{
                new UnitHandler(50, done),
                new UnitHandler(50, done),
                new UnitHandler(50, done)
        };
        setLeftRight(units);

        var threads = Arrays.stream(units).map(Thread::new).toArray(Thread[]::new);

        var barrier = new CustomBarrier(units.length, () -> {
            try {
                Thread.sleep(20);
                System.out.println();

                var modified = false;
                for (int i = 0; i < units.length; i++) {
                    System.out.printf("Handler-%d: %s\n", i, units[i].getLineString());
                    modified = modified || units[i].getModified();
                }
                if (!modified) {
                    done.set(true);
                } else {
                    setLeftRight(units);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        for (var unit : units) {
            unit.setBarrier(barrier);
        }

        for (var thread : threads) {
            thread.start();
        }

        for (var thread : threads) {
            thread.join();
        }
    }

    private static void setLeftRight(UnitHandler[] units) {
        for (int i = 0; i < units.length; i++) {
            if (i == 0) {
                units[i].setLeft(null);
            } else {
                units[i].setLeft(units[i - 1].getRight());
            }

            if (i == units.length - 1) {
                units[i].setRight(null);
            } else {
                units[i].setRight(units[i + 1].getLeft());
            }
        }
    }
}
