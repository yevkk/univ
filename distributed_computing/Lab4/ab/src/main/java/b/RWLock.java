package b;

public class RWLock {
    private int rCounter = 0;
    private int wCounter = 0;
    private int wRequests = 0;

    public synchronized void lockRead() {
        try {
            while (wCounter > 0 || wRequests > 0) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rCounter++;
    }

    public synchronized void unlockRead() {
        rCounter--;
        notifyAll();
    }

    public synchronized void lockWrite() {
        wRequests++;
        try {
            while (rCounter > 0 || wCounter > 0) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wRequests--;
        wCounter++;
    }

    public synchronized void unlockWrite() {
        wCounter--;
        notifyAll();
    }
}