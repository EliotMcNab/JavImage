package com.company.collections.changeAPI.changes.ordered;

import com.company.collections.changeAPI.Change;
import com.company.utilities.ArrayUtil;

import java.util.Arrays;
import java.util.Comparator;

public class SequentialOrdered<E> extends OrderedBase<E> {

    // ====================================
    //               FIELDS
    // ====================================

    private final Change<E>[] changes;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public SequentialOrdered(
            final Class<E> clazz,
            final Change<E>[] changes
    ) {
        super(
                clazz,
                null
        );
        this.changes = changes;
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected boolean canSequentialise(Change<E> change) {
        return false;
    }

    @Override
    protected E[] applyToImpl(E[] array) {
        final Object[] uniqueComparators = ArrayUtil.retainDistinct(getComparators());

        final E[] copy = Arrays.copyOf(array, array.length);

        for (Object comparator : uniqueComparators) {
            Arrays.parallelSort(copy, (Comparator<E>) comparator);
        }

        return copy;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    public Comparator<E>[] getComparators() {
        final Comparator<E>[] comparators = new Comparator[changes.length];
        for (int i = 0; i < changes.length; i++) {
            comparators[i] = ((OrderedBase<E>) changes[i]).getComparator();
        }
        return comparators;
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "SequentialOrdered{comparator=" +
                comparator +
                "}";
    }
}
