package a;

import java.util.concurrent.atomic.AtomicInteger;

public class BeeHive {
    private final Forest forest;
    private final int squadNumber;

    private class ScanForest implements Runnable {
        private final AtomicInteger sync;

        public ScanForest(AtomicInteger sync) {
            this.sync = sync;
        }

        @Override
        public void run() {
            int y;
            while ((y = sync.decrementAndGet()) >= 0) {
                for (int x = 0; x < forest.limX(); x++) {
                    if (forest.checkAndEliminateWinnie(x, y)) {
                        sync.set(0);
                        System.out.printf("Winnie was found and eliminated on: (%d, %d)\n", x, y);
                    }
                }
            }
        }
    }

    public BeeHive(Forest forest, int squadNumber) {
        this.forest = forest;
        this.squadNumber = squadNumber;
    }

    public void  eliminateWinnie() {
        var sync = new AtomicInteger(forest.limY());
        for (int i = 0; i < squadNumber; i++) {
            var scan = new ScanForest(sync);
            (new Thread(scan, "squad-" + i)).start();
        }
    }

}
