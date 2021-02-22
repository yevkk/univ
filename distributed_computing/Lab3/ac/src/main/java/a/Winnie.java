package a;

import java.util.concurrent.CountDownLatch;

public class Winnie implements Runnable {
    private final Pot pot;

    public Winnie(Pot pot) {
        this.pot = pot;
        new Thread(this).start();
    }

    public void run() {
        while (true) {
            try {
                pot.sync.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("\n Winnie had his honey \n");
            pot.sync = new CountDownLatch(pot.getSize());
        }
    }
}
