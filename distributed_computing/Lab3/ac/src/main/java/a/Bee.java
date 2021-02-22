package a;

public class Bee implements Runnable {
    private final Pot pot;

    public Bee(Pot pot) {
        this.pot = pot;
        new Thread(this).start();
    }

    public void run() {
        while(true) {
            if (pot.sync.getCount() != 0) {
                System.out.print(".");
                pot.sync.countDown();
                try {
                    Thread.sleep((int) (Math.random() * (500) + 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
