package com.example.lab6.life;

import java.util.concurrent.atomic.AtomicInteger;

public class Board {
    private CellState[][] cells;
    private final double aliveProbability;
    private final int sizeX;
    private final int sizeY;
    private final int WORKERS = 4;

    public Board(int sizeX, int sizeY, double aliveProbability) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.aliveProbability = aliveProbability;
        this.cells = new CellState[sizeX][sizeY];
        randomFill();
    }

    public void randomFill() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                cells[i][j] = (Math.random() < aliveProbability) ? CellState.ALIVE : CellState.DEAD;
            }
        }
    }

    public CellState[][] generation() {
        CellState[][] newGeneration = new CellState[sizeX][sizeY];
        AtomicInteger sync = new AtomicInteger(sizeX);
        for (int w = 0; w < WORKERS; w++) {
            Runnable generationWorker = () -> {
                int x;
                while ((x = sync.decrementAndGet()) >= 0) {
                    for (int y = 0; y < sizeY; y++) {
                        int aliveCounter = 0;
                        for (int i = Math.max(0, x - 1); i <= Math.min(sizeX - 1, x + 1); i++) {
                            for (int j = Math.max(0, y - 1); j <= Math.min(sizeY - 1, y + 1); y++) {
                                if (cells[i][j] == CellState.ALIVE && !(i == x && j == y)) {
                                    aliveCounter++;
                                }
                            }
                        }
                        newGeneration[x][y] = cells[x][y].next(aliveCounter);
                    }
                }
            };
            new Thread(generationWorker, "generation worker - " + w).start();
        }
        return cells = newGeneration;
    }
}
