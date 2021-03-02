package b;

import java.util.Arrays;
import java.util.Random;

public class Garden {
    public enum PlantState {
        BEST(0.5),
        GOOD(0.5),
        BAD(0);

        private final double degradePossibility;

        PlantState(double degradePossibility) {
            this.degradePossibility = degradePossibility;
        }

        public PlantState degrade() {
            if (new Random().nextDouble() > degradePossibility) {
                return switch (this) {
                    case BEST -> GOOD;
                    case GOOD, BAD -> BAD;
                };
            }
            return this;
        }

        public String toString() {
            return switch (this) {
                case BEST -> "8";
                case GOOD -> "o";
                case BAD -> "*";
            };
        }
    }

    private final PlantState[][] state;

    public Garden(int sizeX, int sizeY) {
        if (sizeX <= 0 || sizeY <= 0) {
            throw new IllegalArgumentException("forest dimensions must be positive numbers");
        }
        state = new PlantState[sizeX][sizeY];
        for (var section : state) {
            for (int y = 0; y < sizeY(); y++) {
                section[y] = PlantState.BEST;
            }
        }
    }

    public int sizeX() {
        return state.length;
    }

    public int sizeY() {
        return state[0].length;
    }

    public void evolution() {
        for (var section : state) {
            for (int y = 0; y < sizeY(); y++) {
                section[y] = section[y].degrade();
            }
        }
    }

    public void fix() {
        for (var section : state) {
            for (int y = 0; y < sizeY(); y++) {
                if (section[y] == PlantState.BAD) {
                    section[y] = PlantState.BEST;
                }
            }
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int y = 0; y < sizeY(); y++) {
            for (int x = 0; x < sizeX(); x++) {
                str.append(" ").append(state[x][y].toString()).append(" ");
            }
            str.append("\n");
        }
        return str.toString();
    }
}
