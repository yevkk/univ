package a;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class DataFileEditor implements Runnable {
    private final File file;
    private final Lock lock;
    private int records = 0;
    private int counter = 0;

    public DataFileEditor(File file, ReadWriteLock rwLock) {
        this.file = file;
        this.lock = rwLock.writeLock();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);
                lock.lock();
                if (Math.random() > 0.5) {
                    addRecord(new Record("Name" + counter, String.valueOf(counter).repeat(4)));
                } else {
                    deleteRecord((int) (Math.random() * (records - 1) + 1));
                }
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Record {
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
            records++;
            writer.write(record.name + " " + record.phone + " ");
            System.out.printf("[%s] added record: (%s, %s)\n", Thread.currentThread().getName(), record.name, record.phone);
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
                if (++i != index) {
                    elements.add(record);
                } else {
                    records--;
                    System.out.printf("[%s] removed record: (%s, %s)\n", Thread.currentThread().getName(), record.name, record.phone);
                }
            }

            var writer = new FileWriter(file, false);
            for (var record : elements) {
                writer.write(record.name + " " + record.phone + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
