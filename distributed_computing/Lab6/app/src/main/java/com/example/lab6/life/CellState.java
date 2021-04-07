package com.example.lab6.life;

public enum CellState {
    ALIVE1, ALIVE2, DEAD;

    public CellState next(int aliveNeighbours1, int aliveNeighbours2) {
        if (this == ALIVE1 && (aliveNeighbours1 == 2 || aliveNeighbours1 == 3)) {
            return ALIVE1;
        }

        if (this == ALIVE2 && (aliveNeighbours2 == 2 || aliveNeighbours2 == 3)) {
            return ALIVE2;
        }

        if (this == DEAD) {
            if ((aliveNeighbours1 == 3)) {
                return ALIVE1;
            } else if (aliveNeighbours2 == 3) {
                return ALIVE2;
            }
        }

        return DEAD;
    }
}
