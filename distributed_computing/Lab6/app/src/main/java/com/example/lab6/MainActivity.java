package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lab6.boardview.BoardView;
import com.example.lab6.life.Board;
import com.example.lab6.life.CellState;

import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Board board = new Board(100, 175, 0.5);
        BoardView boardView = findViewById(R.id.boardView);
        Exchanger<CellState[][]> exchanger = new Exchanger<>();
        AtomicBoolean done = new AtomicBoolean(false);

        boardView.update(board.randomFill());

        new Thread(new BoardUpdate(board, exchanger, done), "Board Generation update").start();
        new Thread(new ViewUpdate(boardView, exchanger, done), "Board View update").start();
    }

    static class BoardUpdate implements Runnable {
        private final Exchanger<CellState[][]> exchanger;
        private final Board board;
        private final AtomicBoolean done;

        BoardUpdate(Board board, Exchanger<CellState[][]> exchanger, AtomicBoolean done) {
            this.exchanger = exchanger;
            this.board = board;
            this.done = done;
        }

        @Override
        public void run() {
            try {
                CellState[][] cells;
                while (!done.get()) {
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
        private final AtomicBoolean done;

        ViewUpdate(BoardView boardView, Exchanger<CellState[][]> exchanger, AtomicBoolean done) {
            this.exchanger = exchanger;
            this.boardView = boardView;
            this.done = done;
        }

        @Override
        public void run() {
            try {
                CellState[][] cells;
                while (!done.get()) {
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