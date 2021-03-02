package b;

import java.util.Random;

class PrinterConsole implements Runnable {
    private final Garden garden;
    private final RWLock lock;

    public PrinterConsole(Garden garden, RWLock lock) {
        this.garden = garden;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(new Random().nextInt(1000) + 1000);
                lock.lockRead();
                System.out.printf("[%s] locked\n", Thread.currentThread().getName());
                Thread.sleep(500);
                System.out.println(garden.toString());
                System.out.printf("[%s] unlocked\n", Thread.currentThread().getName());
                lock.unlockRead();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
