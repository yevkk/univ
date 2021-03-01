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
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new DataFileEditor(file, rwLock, recordsNumber), "Editor").start();
    }
}
