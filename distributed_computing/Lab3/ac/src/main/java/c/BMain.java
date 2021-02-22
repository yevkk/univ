package c;

import java.util.concurrent.Semaphore;

public class BMain {
    public static void main(String[] args) {
        var semaphore = new Semaphore(1, true);
        Component[] table = {null, null};

        new Mediator(semaphore, table);
        new Smoker(semaphore, table, Component.MATCHES);
        new Smoker(semaphore, table, Component.PAPER);
        new Smoker(semaphore, table, Component.TOBACCO);
    }
}
