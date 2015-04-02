package org.kbss.chess.combinator.enums;

import java.util.Arrays;
import java.util.Collection;

import org.kbss.chess.combinator.ChessBoard;

public enum Figure {
    BISHOP("B") {
        @Override
        public boolean isPossibleToSet(ChessBoard board, int row, int column) {
            return board.isDiagonalsFree(row, column);
        }

        @Override
        public void release(ChessBoard board, int row, int column) {
            board.releaseDiagonals(row, column);
            board.release(row, column);
        }

        @Override
        public void set(ChessBoard board, int row, int column) {
            board.reserveDiagonals(row, column);
            board.setFigure(row, column);
        }
    },
    KING("K") {
        final Collection<int[]> offsets = Arrays.asList(new int[] { 0, 0 }, new int[] { -1, 0 }, new int[] { 1, 0 }, new int[] { 0, -1 }, new int[] { 0, 1 },
                new int[] { 1, 1 }, new int[] { 1, -1 }, new int[] { -1, 1 }, new int[] { -1, -1 });

        @Override
        public boolean isPossibleToSet(ChessBoard board, int row, int column) {
            return board.isFree(row, column, offsets);
        }

        @Override
        public void release(ChessBoard board, int row, int column) {
            board.removeOffsets(row, column, offsets);
            board.release(row, column);
        }

        @Override
        public void set(ChessBoard board, int row, int column) {
            board.setOffsets(row, column, offsets);
            board.setFigure(row, column);
        }
    },
    KNIGHT("N") {
        final Collection<int[]> offsets = Arrays.asList(new int[] { 0, 0 }, new int[] { -1, 2 }, new int[] { 1, 2 }, new int[] { 2, -1 }, new int[] { 2, 1 },
                new int[] { -2, 1 }, new int[] { 1, -2 }, new int[] { -2, -1 }, new int[] { -1, -2 });

        @Override
        public boolean isPossibleToSet(ChessBoard board, int row, int column) {
            return isFree(board, row, column);
        }

        private boolean isFree(ChessBoard board, int row, int column) {
            return board.isFree(row, column, offsets);
        }

        @Override
        public void release(ChessBoard board, int row, int column) {
            defaultRelease(board, row, column);
        }

        private void defaultRelease(ChessBoard board, int row, int column) {
            board.removeOffsets(row, column, offsets);
            board.release(row, column);
        }

        @Override
        public void set(ChessBoard board, int row, int column) {
            board.setFigure(row, column);
            board.setOffsets(row, column, offsets);
        }
    },
    QUEEN("Q") {
        @Override
        public boolean isPossibleToSet(ChessBoard board, int row, int column) {
            return board.isDiagonalsFree(row, column) && board.isHorizontalFree(row) && board.isVerticalFree(column);
        }

        @Override
        public void release(ChessBoard board, int row, int column) {
            board.releaseDiagonals(row, column);
            board.releaseDirect(row, column);
            board.release(row, column);
        }

        @Override
        public void set(ChessBoard board, int row, int column) {
            board.reserveDiagonals(row, column);
            defaultSet(board, row, column);
        }
    },
    ROOK("R") {
        @Override
        public boolean isPossibleToSet(ChessBoard board, int row, int column) {
            return board.isHorizontalFree(row) && board.isVerticalFree(column);
        }

        @Override
        public void release(ChessBoard board, int row, int column) {
            board.releaseDirect(row, column);
            board.release(row, column);
        }

        @Override
        public void set(ChessBoard board, int row, int column) {
            defaultSet(board, row, column);
        }
    };

    private static void defaultSet(ChessBoard board, int row, int column) {
        board.reserve(row, column);
        board.setFigure(row, column);
    }

    public static Figure parse(String code) {
        for (Figure figure : values()) {
            if (figure.getCode().equals(code)) {
                return figure;
            }
        }
        return null;
    }

    private final String code;

    private Figure(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public abstract boolean isPossibleToSet(ChessBoard board, int row, int column);

    public abstract void release(ChessBoard board, int row, int column);

    public abstract void set(ChessBoard board, int row, int column);
}