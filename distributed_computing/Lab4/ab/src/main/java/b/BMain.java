package b;

public class BMain {
    public static void main(String[] args) {
        var garden = new Garden(10, 5);
        var lock = new RWLock();

        new Thread(new Nature(garden, lock), "Nature").start();
        new Thread(new Gardener(garden, lock), "Gardener").start();
        new Thread(new PrinterFile(garden, lock), "Print to file").start();
        new Thread(new PrinterConsole(garden, lock), "Print to console").start();
    }
}
