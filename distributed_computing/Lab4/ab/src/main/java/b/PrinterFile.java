package b;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

class PrinterFile implements Runnable {
    private final Garden garden;
    private final RWLock lock;
    private final File file;

    public PrinterFile(Garden garden, RWLock lock) {
        this.garden = garden;
        this.lock = lock;
        this.file = new File("garden.txt");
        FileWriter writer = null;
        try {
            writer = new FileWriter(file, false);
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(new Random().nextInt(1000) + 1000);
                lock.lockRead();
                System.out.printf("[%s] locked\n", Thread.currentThread().getName());
                System.out.printf("[%s] working\n", Thread.currentThread().getName());
                Thread.sleep(500);
                var writer = new FileWriter(file, true);
                writer.write(garden.toString() + "\n");
                writer.flush();
                System.out.printf("[%s] unlocked\n", Thread.currentThread().getName());
                lock.unlockRead();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
