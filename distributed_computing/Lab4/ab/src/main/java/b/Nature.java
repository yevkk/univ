package b;

import java.util.Random;

class Nature implements Runnable {
    private final Garden garden;
    private final RWLock lock;

    public Nature(Garden garden, RWLock lock) {
        this.garden = garden;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(new Random().nextInt(1000) + 1000);
                lock.lockWrite();
                System.out.printf("[%s] locked\n", Thread.currentThread().getName());
                System.out.printf("[%s] working\n", Thread.currentThread().getName());
                garden.evolution();
                System.out.printf("[%s] unlocked\n", Thread.currentThread().getName());
                lock.unlockWrite();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
