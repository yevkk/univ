package a;

import java.util.concurrent.CountDownLatch;

public class Pot {
    public CountDownLatch sync;
    private final int size;

    public Pot(int size) {
        this.size = size;
        sync = new CountDownLatch(this.size);
    }

    public int getSize() {
        return size;
    }
}
