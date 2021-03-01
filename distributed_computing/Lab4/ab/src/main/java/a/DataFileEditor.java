package a;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class DataFileEditor implements Runnable {
    private final File file;
    private final Lock lock;
    private final AtomicInteger recordsNumber;
    private int counter = 0;

    public DataFileEditor(File file, ReadWriteLock rwLock, AtomicInteger recordsNumber) {
        this.file = file;
        this.lock = rwLock.writeLock();
        this.recordsNumber  = recordsNumber;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                System.out.printf("[%s] locked\n", Thread.currentThread().getName());
                if (Math.random() < 0.6) {
                    addRecord(new Record("Name" + (counter + 1), String.valueOf(counter + 1).repeat(4)));
                } else {
                    int index = (int) (Math.random() * (recordsNumber.get() - 1) + 1);
                    System.out.printf("[%s] deleting %d\n", Thread.currentThread().getName(), index);
                    deleteRecord(index);
                }
                System.out.printf("[%s] unlocked\n", Thread.currentThread().getName());
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Record {
        String name;
        String phone;

        Record(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }
    }

    private void addRecord(Record record) {
        try {
            var writer = new FileWriter(file, true);
            counter++;
            recordsNumber.incrementAndGet();
            writer.write(record.name + " " + record.phone + " ");
            System.out.printf("[%s] added record: (%s, %s)\n", Thread.currentThread().getName(), record.name, record.phone);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteRecord(int index) {
        try {
            var scanner = new Scanner(file);
            var elements = new ArrayList<Record>();
            int i = 0;
            while (scanner.hasNext()) {
                var record = new Record(scanner.next(), scanner.next());
                i++;
                if (i != index) {
                    elements.add(record);
                } else {
                    recordsNumber.decrementAndGet();
                    System.out.printf("[%s] removed record: (%s, %s)\n", Thread.currentThread().getName(), record.name, record.phone);
                }
            }

            var writer = new FileWriter(file, false);
            for (var record : elements) {
                writer.write(record.name + " " + record.phone + " ");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
