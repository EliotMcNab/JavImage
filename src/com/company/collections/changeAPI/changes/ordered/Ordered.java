package com.company.collections.changeAPI.changes.ordered;

import com.company.collections.changeAPI.Change;
import com.company.utilities.comparators.ObjectComparator;

import java.util.Arrays;
import java.util.Comparator;

public class Ordered<E> extends OrderedBase<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public Ordered(
            final Class<E> clazz
    ) {
        super(
                clazz,
                (Comparator<E>) new ObjectComparator()
        );
    }

    public Ordered(
            final Class<E> clazz,
            final Change<E> parent
    ) {
        super(
                clazz,
                (Comparator<E>) new ObjectComparator(),
                parent
        );
    }

    public Ordered(
            final Class<E> clazz,
            final Comparator<E> comparator
    ) {
        super(
                clazz,
                comparator
        );
    }

    public Ordered(
            final Class<E> clazz,
            final Comparator<E> comparator,
            final Change<E> parent
    ) {
        super(
                clazz,
                comparator,
                parent
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected E[] applyToImpl(E[] array) {
        final E[] copyArray = Arrays.copyOf(array, array.length);
        Arrays.parallelSort(copyArray, comparator);
        return copyArray;
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "Ordered{comparator=" +
                comparator +
                "}";
    }
}
