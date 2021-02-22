package c;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Mediator implements Runnable {
    private final List<Component> components = new ArrayList<>();
    private final Semaphore semaphore;
    private final Component[] table;

    public Mediator(Semaphore semaphore, Component[] table) {
        this.table = table;
        this.semaphore = semaphore;
        components.add(Component.MATCHES);
        components.add(Component.PAPER);
        components.add(Component.TOBACCO);
        new Thread(this).start();
    }

    public void run() {
        while (true) {
            try {
                semaphore.acquire();
                if (table[0] == null && table[1] == null) {
                    Collections.shuffle(components);
                    table[0] = components.get(0);
                    table[1] = components.get(1);
                    Thread.sleep(1000);
                    System.out.println("\nMediator put on the table " + table[0] + " and " + table[1]);
                }
                semaphore.release();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }
}
