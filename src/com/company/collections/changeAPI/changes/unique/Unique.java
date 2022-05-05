package com.company.collections.changeAPI.changes.unique;

import com.company.collections.changeAPI.Change;
import com.company.utilities.ArrayUtil;
import com.company.utilities.comparators.ObjectComparator;

import java.util.Comparator;

public class Unique<E> extends UniqueBase<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public Unique(
            final Class<E> clazz
    ) {
        super(
                clazz,
                (Comparator<E>) new ObjectComparator()
        );
    }

    public Unique(
            final Class<E> clazz,
            final Change<E> parent
    ) {
        super(
                clazz,
                (Comparator<E>) new ObjectComparator(),
                parent
        );
    }

    public Unique(
            final Class<E> clazz,
            final Comparator<E> comparator
    ) {
        super(
                clazz,
                comparator
        );
    }

    public Unique(
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
        return ArrayUtil.retainDistinct(array, comparator);
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "Unique{comparator=" +
                comparator +
                "}";
    }
}
