package a;

public class AMain {
    private static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static void main(String[] args) {
        var forest = new Forest(Config.forestSizeX, Config.forestSizeY);
        var hive = new BeeHive(forest, Config.squadNumber);

        int x = randomInt(0, forest.sizeX());
        int y = randomInt(0, forest.sizeY());

        forest.placeWinnie(x, y);
        System.out.printf("Winnie went for a walk to: (%d, %d)\n", x, y);
        hive.eliminateWinnie();
    }
}
