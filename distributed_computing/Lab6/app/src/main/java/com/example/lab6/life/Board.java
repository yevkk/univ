package com.example.lab6.life;

import java.util.concurrent.atomic.AtomicInteger;

public class Board {
    private CellState[][] cells;
    private final double aliveProbability1, aliveProbability2;
    private final int sizeX;
    private final int sizeY;
    private final int WORKERS = 4;

    public Board(int sizeX, int sizeY, double aliveProbability1, double aliveProbability2) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.aliveProbability1 = aliveProbability1;
        this.aliveProbability2 = aliveProbability2;
        this.cells = new CellState[sizeX][sizeY];
    }

    public CellState[][] randomFill() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                cells[i][j] = (Math.random() < aliveProbability1) ? CellState.ALIVE1 : ((Math.random() < aliveProbability2) ? CellState.ALIVE2 : CellState.DEAD);
            }
        }
        return cells;
    }

    public CellState[][] generation() {
        CellState[][] newGeneration = new CellState[sizeX][sizeY];
        AtomicInteger sync = new AtomicInteger(sizeX);
//        for (int w = 0; w < WORKERS; w++) {
//            Runnable generationWorker = () -> {
//
//            };
//            new Thread(generationWorker, "generation worker - " + w).start();
//        }

        int x;
        while ((x = sync.decrementAndGet()) >= 0) {
            for (int y = 0; y < sizeY; y++) {
                int aliveCounter1 = 0;
                int aliveCounter2 = 0;
                for (int i = Math.max(0, x - 1); i <= Math.min(sizeX - 1, x + 1); i++) {
                    for (int j = Math.max(0, y - 1); j <= Math.min(sizeY - 1, y + 1); j++) {
                        if (!(i == x && j == y)) {
                            if (cells[i][j] == CellState.ALIVE1) {
                                aliveCounter1++;
                            } else if (cells[i][j] == CellState.ALIVE2) {
                                aliveCounter2++;
                            }
                        }
                    }
                }
                newGeneration[x][y] = cells[x][y].next(aliveCounter1, aliveCounter2);
            }
        }

        cells = newGeneration;
        return cells;
    }
}
