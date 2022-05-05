package com.company.collections.changeAPI.changes.ordered;

import com.company.collections.changeAPI.Change;
import com.company.collections.changeAPI.changes.ComparingChange;

import java.util.Comparator;

public abstract class OrderedBase<E> extends ComparingChange<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public OrderedBase(
            final Class<E> clazz,
            final Comparator<E> comparator
    ) {
        super(
                clazz,
                comparator
        );
    }

    public OrderedBase(
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
    protected Change<E> toSequential(Change<E>[] changes) {
        return new SequentialOrdered<>(clazz, changes);
    }

}
