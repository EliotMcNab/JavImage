package com.company.collections.changeAPI.changes;

import com.company.collections.changeAPI.Change;

import java.util.Comparator;

public abstract class ComparingChange<E> extends Change<E> {

    // ====================================
    //               FIELDS
    // ====================================

    protected final Comparator<E> comparator;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ComparingChange(
            final Class<E> clazz,
            final Comparator<E> comparator
    ) {
        super(clazz);
        this.comparator = comparator;
    }

    public ComparingChange(
            final Class<E> clazz,
            final Comparator<E> comparator,
            final Change<E> parent
    ) {
        super(
                clazz,
                parent
        );
        this.comparator = comparator;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    public Comparator<E> getComparator() {
        return comparator;
    }
}
