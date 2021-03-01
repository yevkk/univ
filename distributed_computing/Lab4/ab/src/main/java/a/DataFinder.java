package a;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class DataFinder implements Runnable {
    private final File file;
    private final Lock lock;
    private final AtomicInteger recordsNumber;

    public DataFinder(File file, ReadWriteLock rwLock, AtomicInteger recordsNumber) {
        this.file = file;
        this.lock = rwLock.readLock();
        this.recordsNumber = recordsNumber;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(Utils.randomInt(1500, 2500));
                lock.lock();
                System.out.printf("[%s] locked\n", Thread.currentThread().getName());
                int index = Utils.randomInt(1, recordsNumber.get());
                System.out.printf("[%s] looking for record with index %d\n", Thread.currentThread().getName(), index);
                var record = getRecord(index);
                Thread.sleep(500);
                if (record != null) {
                    System.out.printf("[%s] found record: (%s, %s)\n", Thread.currentThread().getName(), record.name, record.phone);
                }
                System.out.printf("[%s] unlocked\n", Thread.currentThread().getName());
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Utils.Record getRecord(int index) {
        try {
            var scanner = new Scanner(file);
            int i = 0;
            while (scanner.hasNext()) {
                var record = new Utils.Record(scanner.next(), scanner.next());
                i++;
                if (i == index) {
                    return record;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getName(String phone) {
        try {
            var scanner = new Scanner(file);
            while (scanner.hasNext()) {
                var record = new Utils.Record(scanner.next(), scanner.next());
                if (phone.equals(record.phone)) {
                    return record.name;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPhone(String name) {
        try {
            var scanner = new Scanner(file);
            while (scanner.hasNext()) {
                var record = new Utils.Record(scanner.next(), scanner.next());
                if (name.equals(record.name)) {
                    return record.phone;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
