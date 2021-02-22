package a;

public class AMain {
    public static void main(String[] args) {
        var pot = new Pot(Config.potSize);
        new Winnie(pot);
        for (int i = 0; i < Config.beesNumber; i++) {
            new Bee(pot);
        }
    }
}
