package org.kbss.chess.combinator;

import java.util.HashMap;
import java.util.Map;

import org.kbss.chess.combinator.enums.Figure;

class Main {

    private static void info(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Integer width, height;
        if (args.length > 0 && args.length >= 3) {
            width = Integer.valueOf(args[0]);
            height = Integer.valueOf(args[1]);
            Map<Figure, Integer> figures = parseFigures(args);
            int combinations = new CombinationCalculator().calculate(width, height, figures);
            info("Unique combinations: " + combinations);
            info("Time: " + (System.currentTimeMillis() - start) / 1000f + "s.");
        }
    }

    private static Map<Figure, Integer> parseFigures(String[] args) {
        Map<Figure, Integer> figures = new HashMap<Figure, Integer>();
        for (int i = 2; i < args.length; i++) {
            String arg = args[i];
            Figure figure = Figure.parse(String.valueOf(arg.charAt(0)));
            figures.put(figure, Integer.parseInt(arg.substring(1)));
        }
        return figures;
    }
}