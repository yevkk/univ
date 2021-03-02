package b;

public class RWLock {
    private int rCounter = 0;
    private int wCounter = 0;
    private int wRequests = 0;

    public synchronized void lockRead() throws InterruptedException {
        while (wCounter > 0 || wRequests > 0) {
            wait();
        }
        rCounter++;
    }

    public synchronized void unlockRead() {
        rCounter--;
        notifyAll();
    }

    public synchronized void lockWrite() throws InterruptedException {
        wRequests++;

        while (rCounter > 0 || wCounter > 0) {
            wait();
        }
        wRequests--;
        wCounter++;
    }

    public synchronized void unlockWrite() throws InterruptedException {
        wCounter--;
        notifyAll();
    }
}