package b;

public class PCItemQueue {
    private Item item;
    private boolean valueSet = false;

    synchronized Item get() {
        while (!valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }

        valueSet = false;
        notify();
        return item;
    }

    synchronized void put(Item item) {
        while(valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }

        this.item = item;
        valueSet = true;
        notify();
    }
}
