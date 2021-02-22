package c;

import java.util.concurrent.Semaphore;

public class Smoker implements Runnable{
    private final Component component;
    private final Semaphore semaphore;
    private final Component[] table;

    public Smoker(Semaphore semaphore, Component[] table, Component component) {
        this.component = component;
        this.table = table;
        this.semaphore = semaphore;
        new Thread(this).start();
    }

    public void run() {
        while (true) {
            try {
                semaphore.acquire();
                if (table[0] != null && table[1] != null && table[0] != component && table[1] != component) {
                    table[0] = table[1] = null;
                    Thread.sleep(500);
                    System.out.println("Smoker with " + component + " takes components from the table");
                } else {
                    System.out.println("...");
                }
                semaphore.release();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }
}
