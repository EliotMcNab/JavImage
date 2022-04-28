package com.company.utilities.comparators;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Used to compare two 1D arrays together where only the nth element is taken into consideration
 */
public class ArrayElementComparator implements Comparator<Object[]> {
    private final int index;

    public ArrayElementComparator(
            final int index
    ) {
        this.index = index;
    }

    @Override
    public int compare(Object[] o1, Object[] o2) {
        try {
            return ((Comparable<Object>) o1[index]).compareTo(o2[index]);
        } catch (ClassCastException e) {
            return Integer.compare(o1[index].hashCode(), o2[index].hashCode());
        }
    }
}
