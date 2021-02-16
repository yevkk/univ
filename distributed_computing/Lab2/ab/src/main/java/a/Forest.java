package a;

import java.util.Arrays;

public class Forest {
    private final boolean[][] space;
    private boolean WinniePlaced;

    Forest(int sizeX, int sizeY) {
        if (sizeX <= 0 || sizeY <= 0) {
            throw new IllegalArgumentException("forest dimensions must be positive numbers");
        }
        space = new boolean[sizeX][sizeY];
        for(var sectionBlock : space) {
            Arrays.fill(sectionBlock, false);
        }

        WinniePlaced = false;
    }

    public boolean placeWinnie(int x, int y) {
        if (WinniePlaced) {
            return false;
        } else {
            WinniePlaced = true;
            space[x][y] = true;
            return true;
        }
    }

    public boolean checkAndEliminateWinnie(int x, int y) {
        if (space[x][y]) {
            space[x][y] = false;
            return true;
        } else {
            return false;
        }
    }

    public int limX() {
        return space.length;
    }

    public int limY() {
        return space[0].length;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (var sectionBlock : space) {
            for (var section : sectionBlock) {
                stringBuilder.append(section ? "x" : "o").append(" ");
            }
            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }
}
