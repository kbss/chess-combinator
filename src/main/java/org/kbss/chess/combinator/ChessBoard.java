package org.kbss.chess.combinator;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

public class ChessBoard {
    private final int[][] grid;
    private final int[] horizontal;
    private final int[] left;
    private final int[] right;
    private final int[] verticals;
    private final int width;
    private final int height;

    private static final String NEW_LINE = "\n";
    private static final String EMPTY_CELL = "_";
    private static final String CELL_SEPARATOR = "|";
    private final Deque<Cell> cells = new ArrayDeque<Cell>();

    public ChessBoard(int width, int height) {
        this.width = width;
        this.height = height;

        grid = new int[height][width];
        verticals = new int[width];
        horizontal = new int[height];
        left = new int[width + height - 1];
        right = new int[width + height - 1];
    }

    public void setOffsets(int row, int column, Collection<int[]> offsets) {
        offset(row, column, offsets, 1);
    }

    public boolean isFree(int row, int column, Collection<int[]> offsets) {
        for (int[] offset : offsets) {
            if (row + offset[0] < height && column + offset[1] < width && row + offset[0] >= 0 && column + offset[1] >= 0) {
                return isFree(row + offset[0], column + offset[1]);
            }
        }
        return true;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isDiagonalsFree(int row, int column) {
        int startRow1 = row - Math.min(row, column);
        int startColumn1 = column - Math.min(row, column);

        int startRow2 = row + Math.min(grid.length - row - 1, column);
        int startColumn2 = column - Math.min(grid.length - row - 1, column);

        return left[startColumn1 == 0 ? startRow1 : grid.length - 1 + startColumn1] == 0
                && right[startColumn2 == 0 ? startRow2 : grid.length - 1 + startColumn2] == 0;
    }

    boolean isFree(int row, int column) {
        return grid[row][column] != -1;
    }

    public boolean isFreeToPut(int row, int column) {
        return grid[row][column] == 0;
    }

    public boolean isHorizontalFree(int row) {
        return horizontal[row] == 0;
    }

    public boolean isVerticalFree(int column) {
        return verticals[column] == 0;
    }

    private void offset(int row, int column, Collection<int[]> offsets, int d) {
        for (int[] offset : offsets) {
            if (row + offset[0] < height && row + offset[0] >= 0 && column + offset[1] < width && column + offset[1] >= 0) {
                grid[row + offset[0]][column + offset[1]] += d;
            }
        }
    }

    public void release(int row, int column) {
        grid[row][column] = 0;
        setDiagonals(row, column, -1);
        setDirect(row, column, -1);
    }

    public void releaseDiagonals(int row, int column) {
        setDiagonal(row, column, -1);
    }

    public void releaseDirect(int row, int column) {
        reserve(row, column, -1);
    }

    public void reserve(int row, int column) {
        reserve(row, column, 1);
    }

    private void reserve(int row, int column, int d) {
        for (int r = 0; r < grid.length; r++) {
            grid[r][column] += d;
        }
        for (int col = 0; col < grid[row].length; col++) {
            grid[row][col] += d;
        }
    }

    public void reserveDiagonals(int row, int column) {
        setDiagonal(row, column, 1);
    }

    private void setDiagonal(int row, int column, int d) {
        int startRow1 = row - Math.min(row, column);
        int startColumn1 = column - Math.min(row, column);

        int startRow2 = row + Math.min(grid.length - row - 1, column);
        int startColumn2 = column - Math.min(grid.length - row - 1, column);

        for (int i = 0; i <= Math.min(height - startRow1 - 1, width - startColumn1 - 1); i++) {
            grid[startRow1 + i][startColumn1 + i] += d;
        }

        for (int i = 0; i <= Math.min(startRow2, width - startColumn2 - 1); i++) {
            grid[startRow2 - i][startColumn2 + i] += d;
        }
    }

    void setDiagonals(int row, int column, int offset) {
        int firstRow = row - Math.min(row, column);
        int firstColumn = column - Math.min(row, column);

        int secondRow = row + Math.min(grid.length - row - 1, column);
        int secondColumn = column - Math.min(grid.length - row - 1, column);

        left[firstColumn == 0 ? firstRow : grid.length - 1 + firstColumn] += offset;
        right[secondColumn == 0 ? secondRow : grid.length - 1 + secondColumn] += offset;
    }

    void setDirect(int row, int column, int offset) {
        verticals[column] += offset;
        horizontal[row] += offset;
    }

    public void setFigure(int row, int column) {
        grid[row][column] = -1;
        setDiagonals(row, column, 1);
        setDirect(row, column, 1);
    }

    public void removeOffsets(int row, int column, Collection<int[]> offsets) {
        offset(row, column, offsets, -1);
    }

    public void push(Cell cell) {
        cells.push(cell);
    }

    public void pop() {
        cells.pop();
    }

    @Override
    public String toString() {
        String[][] grid = new String[height][width];
        StringBuilder result = new StringBuilder();
        for (Cell cell : cells) {
            grid[cell.getRow()][cell.getColumn()] = cell.getFigure().getCode();
        }
        for (int row = 0; row < height; row++) {
            result.append(" " + EMPTY_CELL);
        }
        result.append(NEW_LINE);
        for (int row = 0; row < height; row++) {
            result.append(CELL_SEPARATOR);
            for (int column = 0; column < width; column++) {
                if (column > 0) {
                    result.append(CELL_SEPARATOR);
                }
                if (grid[row][column] != null) {
                    result.append(grid[row][column]);
                } else {
                    result.append(EMPTY_CELL);
                }
            }
            result.append(CELL_SEPARATOR);
            result.append(NEW_LINE);
        }
        return result.toString();
    }
}
