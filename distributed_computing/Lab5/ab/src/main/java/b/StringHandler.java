package b;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class StringHandler implements Runnable {
    private StringBuilder str;
    private final CyclicBarrier barrier;
    private final AtomicBoolean done;
    private int abCounter;

    public StringHandler(int length, CyclicBarrier barrier, AtomicBoolean done) {
        this.barrier = barrier;
        this.done = done;
        this.abCounter = 0;

        this.str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            var rand = Math.random();
            str.append((rand < 0.25) ? "A" : ((rand < 0.5) ? "B" : ((rand < 0.75) ? "C" : "D")));
        }
    }

    public int getCounter() {
        return abCounter;
    }

    public String getString() {
        return str.toString();
    }

    @Override
    public void run() {
        try {
            while (!done.get()) {
                abCounter = 0;

                for (int i = 0; i < str.length(); i++) {
                    if (Math.random() < 0.5) {
                        str.setCharAt(i, switch (str.charAt(i)) {
                            case 'A' -> 'C';
                            case 'C' -> 'A';
                            case 'B' -> 'D';
                            case 'D' -> 'B';
                            default -> ' ';
                        });
                    }
                    if (str.charAt(i) == 'A' || str.charAt(i) == 'B') {
                        abCounter++;
                    }
                }
                barrier.await();
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
