package org.kbss.chess.combinator;

import org.kbss.chess.combinator.enums.Figure;

class Cell {
    private final int column;
    private final Figure figure;
    private final int row;

    public Cell(Figure figure, int row, int column) {
        this.figure = figure;
        this.row = row;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public Figure getFigure() {
        return figure;
    }

    public int getRow() {
        return row;
    }
}