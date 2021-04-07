package com.example.lab6.life;

public enum CellState {
    ALIVE1, DEAD;
    public CellState next(int aliveNeighbours) {
        if (aliveNeighbours == 3 || (aliveNeighbours == 2 && this == ALIVE1)) {
            return ALIVE1;
        } else {
            return DEAD;
        }
    }
}
