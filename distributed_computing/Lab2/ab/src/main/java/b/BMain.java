package b;

import b.burglars.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class BMain {
    private static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    private static int fillStorage(Queue<Item> storage) {
        int res = 0;
        for (int i = 0; i < Config.storageSize; i++) {
            int cost = randomInt(Config.minCost, Config.maxCost);
            res += cost;
            storage.add(new Item(cost));
        }
        return res;
    }

    public static void main(String[] args) {
        Queue<Item> storage = new LinkedList<Item>();
        int sum = fillStorage(storage);
        System.out.println("Storage is filled with products, total cost: " + sum);

        var midpoint = new PCItemQueue();
        var truck = new PCItemQueue();

        var Ivanov = new FirstBurglar(storage, midpoint);
        var Petrov = new MidBurglar(midpoint, truck);
        var Nechiporuk = new LastBurglar(truck);

        try {
            Ivanov.getThread().join();
            Petrov.getThread().join();
            Nechiporuk.getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Truck is loaded, total cost: " + Nechiporuk.getSum());
    }






}
