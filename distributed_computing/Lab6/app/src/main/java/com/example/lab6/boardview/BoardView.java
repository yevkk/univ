package com.example.lab6.boardview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.lab6.R;
import com.example.lab6.life.CellState;

public class BoardView extends View {
    private CellState[][] cells;
    private final Paint paint;
    private float cellSize;
    private float hPadding, vPadding;

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        cells = new CellState[1][1];
    }

    public void setCells(CellState[][] cells) {
        this.cells = cells;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (cells.length == 0 || cells[0].length == 0) {
            return;
        }
        int sizeX = cells.length;
        int sizeY = cells[0].length;
        cellSize = Math.min(this.getMeasuredWidth() / (float) sizeX, this.getMeasuredHeight() / (float) sizeY);
        hPadding = (this.getMeasuredWidth() - sizeX * cellSize) / 2;
        vPadding = (this.getMeasuredHeight() - sizeY * cellSize) / 2;

        paint.setColor(getResources().getColor(R.color.black, null));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        canvas.drawRect(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight(), paint);

        paint.setColor(getResources().getColor(R.color.lifeCell, null));
        drawCells(canvas);
    }

    private void drawCells(Canvas canvas) {
        int sizeX = cells.length;
        int sizeY = cells[0].length;
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (cells[i][j] == CellState.ALIVE1) {
                    float x = hPadding + cellSize * i;
                    float y = vPadding + cellSize * j;
                    canvas.drawRect(x, y, x + cellSize, y + cellSize, paint);
                }
            }
        }
    }

    public void update(CellState[][] cells) {
        this.cells = cells;
        this.postInvalidate();
    }
}
