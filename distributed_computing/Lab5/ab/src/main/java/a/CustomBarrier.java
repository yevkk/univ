package a;

class CustomBarrier {
    private final int parties;
    private int waitingFor;
    private final Runnable barrierAction;

    public CustomBarrier(int parties, Runnable barrierAction) {
        this.parties = waitingFor = parties;
        this.barrierAction = barrierAction;
    }

    public synchronized void await() throws InterruptedException {
        waitingFor--;

        if (waitingFor > 0) {
            this.wait();
        } else {
            waitingFor = parties;
            notifyAll();
            barrierAction.run();
        }
    }
}