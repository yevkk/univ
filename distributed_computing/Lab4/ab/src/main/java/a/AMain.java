package a;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AMain {
    public static void main(String[] args) {
        var rwLock = new ReentrantReadWriteLock();
        var recordsNumber = new AtomicInteger();

        var file = new File("data.txt");
        try {
            if(file.delete()) {
                System.out.println("File deleted");
            }
            if(file.createNewFile()) {
                System.out.println("File created");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new DataFileEditor(file, rwLock, recordsNumber), "Editor").start();
        new Thread(new DataFinder(file, rwLock, recordsNumber), "Finder-1").start();
        new Thread(new DataFinder(file, rwLock, recordsNumber), "Finder-2").start();
    }
}
