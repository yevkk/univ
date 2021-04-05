package com.example.lab6.life;

public enum CellState {
    ALIVE, DEAD;
    public CellState next(int aliveNeighbours) {
        if (aliveNeighbours == 3 || (aliveNeighbours == 2 && this == ALIVE)) {
            return ALIVE;
        } else {
            return DEAD;
        }
    }
}
