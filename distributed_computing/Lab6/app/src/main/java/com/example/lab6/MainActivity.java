package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.lab6.boardview.BoardView;
import com.example.lab6.life.Board;
import com.example.lab6.life.CellState;

import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {
    private AtomicBoolean stop;
    private boolean isRunning;
    private final int SIZE_X = 200;
    private final int SIZE_Y = 320;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stop = new AtomicBoolean(false);
        isRunning = false;
    }

    public void startSingleCiv(View view) {
        if (isRunning) {
            isRunning = false;
            stop.set(true);
        } else {
            isRunning = true;
            stop.set(false);
            Board board = new Board(SIZE_X, SIZE_Y, 0.2, 0);
            startCiv(board);
        }
    }

    public void startDoubleCiv(View view) {
        if (isRunning) {
            isRunning = false;
            stop.set(true);
        } else {
            isRunning = true;
            stop.set(false);
            Board board = new Board(SIZE_X, SIZE_Y, 0.2, 0.2);
            startCiv(board);
        }
    }

    private void startCiv(Board board) {
        BoardView boardView = findViewById(R.id.boardView);
        Exchanger<CellState[][]> exchanger = new Exchanger<>();
        boardView.update(board.randomFill());

        new Thread(new BoardUpdate(board, exchanger, stop), "Board Generation update").start();
        new Thread(new ViewUpdate(boardView, exchanger, stop), "Board View update").start();
    }

    static class BoardUpdate implements Runnable {
        private final Exchanger<CellState[][]> exchanger;
        private final Board board;
        private final AtomicBoolean stop;

        BoardUpdate(Board board, Exchanger<CellState[][]> exchanger, AtomicBoolean stop) {
            this.exchanger = exchanger;
            this.board = board;
            this.stop = stop;
        }

        @Override
        public void run() {
            try {
                CellState[][] cells;
                while (!stop.get()) {
                    cells = board.generation();
                    exchanger.exchange(cells);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class ViewUpdate implements Runnable {
        private final Exchanger<CellState[][]> exchanger;
        private final BoardView boardView;
        private final AtomicBoolean stop;

        ViewUpdate(BoardView boardView, Exchanger<CellState[][]> exchanger, AtomicBoolean stop) {
            this.exchanger = exchanger;
            this.boardView = boardView;
            this.stop = stop;
        }

        @Override
        public void run() {
            try {
                CellState[][] cells;
                while (!stop.get()) {
                    cells = exchanger.exchange(null);
                    boardView.update(cells);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}