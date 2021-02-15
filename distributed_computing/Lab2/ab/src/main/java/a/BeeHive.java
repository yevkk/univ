package a;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BeeHive {
    private final Forest forest;
    private final int squadNumber;

    private class ScanForest implements Runnable {
        private final int startFrom;
        private final int blockSize;
        private final AtomicInteger sync;
        private final AtomicBoolean WinnieWasFound;

        public ScanForest(int startFrom, int blockSize, AtomicInteger sync, AtomicBoolean WinnieWasFound) {
            this.startFrom = startFrom;
            this.blockSize = blockSize;
            this.sync = sync;
            this.WinnieWasFound = WinnieWasFound;
        }

        @Override
        public void run() {
            for (int i = startFrom; i < startFrom + blockSize && !WinnieWasFound.get(); i++) {
                for (int j = 0; j < forest.sizeY() && !WinnieWasFound.get(); j++) {
                    if (forest.checkAndEliminateWinnie(i, j)) {
                        System.out.printf("Winnie was found and eliminated on: (%d, %d)\n", i, j);
                        WinnieWasFound.set(true);
                    }
                }
            }
            sync.incrementAndGet();
        }
    }

    public BeeHive(Forest forest, int squadNumber) {
        this.forest = forest;
        this.squadNumber = squadNumber;
    }

    public void  eliminateWinnie() {
        var sync = new AtomicInteger(0);
        var WinnieWasFound = new AtomicBoolean(false);
        int unassigned = forest.sizeX();
        for (int i = 0; i < squadNumber; i++) {
            var scan = new ScanForest(forest.sizeX() - unassigned, unassigned / (squadNumber - i), sync, WinnieWasFound);
            (new Thread(scan, "squad-" + i)).start();
        }

        while (sync.get() != squadNumber) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
