package com.company.models;

import com.company.Grid;

import java.util.Random;

public interface GenerationModel<T> {
    Grid<T> generate(final int width, final int height);

    // ===================================
    //             CONSTANTS
    // ===================================

    /**
     * Generates random values along a grid
     */
    GenerationModel<Double> RANDOM = (width, height) -> {
        final Grid<Double> grid = new Grid<>(width, height);

        final Random random = new Random();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < height; x++) {
                grid.setValueAt(x, y, random.nextDouble());
            }
        }

        return grid;
    };

    /**
     * Goes from a value of 0 (left) to width (right)
     */
    GenerationModel<Double> GRADIENT_LEFT = (width, height) -> {
        final Grid<Double> grid = new Grid<>(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid.setValueAt(x, y, (double) x);
            }
        }

        return grid;
    };

    /**
     * Goes from a value of width (left) to 0 (right)
     */
    GenerationModel<Double> GRADIENT_RIGHT = (width, height) -> {
        final Grid<Double> grid = new Grid<>(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid.setValueAt(x, y, (double) (width - x));
            }
        }

        return grid;
    };

    /**
     * Goes from a value of 0 (top) to height (bottom)
     */
    GenerationModel<Double> GRADIENT_TOP = (width, height) -> {
        final Grid<Double> grid = new Grid<>(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid.setValueAt(x, y, (double) y);
            }
        }

        return grid;
    };

    /**
     * Goes from a value of height (top) to 0 (bottom)
     */
    GenerationModel<Double> GRADIENT_BOTTOM = (width, height) -> {
        final Grid<Double> grid = new Grid<>(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < height; x++) {
                grid.setValueAt(x, y, (double) height - y);
            }
        }

        return grid;
    };
}
