package a;

public class AMain {
    private static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static void main(String[] args) {
        var forest = new Forest(Config.forestMaxX, Config.forestMaxY);
        var hive = new BeeHive(forest, Config.squadNumber);

        int x = randomInt(0, forest.limX());
        int y = randomInt(0, forest.limY());

        forest.placeWinnie(x, y);
        System.out.printf("Winnie went for a walk to: (%d, %d)\n", x, y);
        hive.eliminateWinnie();
    }
}
