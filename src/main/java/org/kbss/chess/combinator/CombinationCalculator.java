package org.kbss.chess.combinator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import org.kbss.chess.combinator.enums.Figure;

class CombinationCalculator {
    private int count;

    private void processRecursive(ChessBoard board, Collection<Figure> figures, boolean isSame, int width, int lastColumn) {
        Deque<Figure> clone = new ArrayDeque<Figure>();
        clone.addAll(figures);
        if (!clone.isEmpty()) {
            Figure figure = clone.pop();
            for (int row = isSame ? width : 0; row < board.getHeight(); row++) {
                int column = isSame && row == width ? lastColumn + 1 : 0;
                for (int i = column; i < board.getWidth(); i++) {
                    placeFigure(board, figures, clone, figure, row, i);
                }
            }
        } else {
            count++;
            System.out.println(board);
        }
    }

    private void placeFigure(ChessBoard board, Collection<Figure> figures, Deque<Figure> clone, Figure figure, int row, int column) {
        if (board.isFreeToPut(row, column) && figure.isPossibleToSet(board, row, column)) {
            figure.set(board, row, column);
            board.push(new Cell(figure, row, column));
            boolean same = clone.size() > 1 && figures.iterator().next() == figure;
            processRecursive(board, clone, same, row, column);
            board.pop();
            figure.release(board, row, column);
        }
    }

    private List<Figure> multiply(Map<Figure, Integer> figuresMap) {
        List<Figure> result = new ArrayList<Figure>();
        for (Map.Entry<Figure, Integer> entry : figuresMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public int calculate(int width, int height, Map<Figure, Integer> figuresMap) {
        count = 0;
        processRecursive(new ChessBoard(width, height), multiply(figuresMap), false, 0, 0);
        return count;
    }
}