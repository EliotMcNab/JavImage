package com.company;

public class Grid<T> {

    // ===================================
    //               FIELDS
    // ===================================

    private final int width;
    private final int height;
    private final T[] grid;

    // ===================================
    //            CONSTRUCTOR
    // ===================================

    public Grid(final int width, final int height) {
        this.width = width;
        this.height = height;
        grid = (T[]) new Object[this.width * this.height];
    }

    private Grid(final int width, final int height, final T[] grid) {
        this.width = width;
        this.height = height;
        this.grid = grid;
    }

    // ===================================
    //               TILING
    // ===================================

    private int loopX(final int x) {
        if (x < 0) return x + width * (-x / width + 1);
        else if (x > width) return x % width;
        else return x;
    }

    private int loopY(final int y) {
        if (y < 0) return y + height * (-y / height + 1);
        else if (y > height) return y % height;
        else return y;
    }

    private int get1DCoordinates(final int x, final int y) {
        return loopX(x) * loopY(y);
    }

    // ===================================
    //             ACCESSORS
    // ===================================

    public T getValueAt(final int x, final int y) {
        return grid[get1DCoordinates(x, y)];
    }

    public void setValueAt(final int x, final int y, final T t) {
        grid[get1DCoordinates(x, y)] = t;
    }
}
