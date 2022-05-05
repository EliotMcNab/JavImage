package com.company.collections.changeAPI.changes.replace;

import com.company.collections.changeAPI.Change;
import com.company.collections.changeAPI.changes.ConditionalChange;

import java.util.function.Predicate;

public abstract class ReplaceBase<E> extends ConditionalChange<E> {

    // ====================================
    //               FIELDS
    // ====================================

    protected final Object[] toReplace;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ReplaceBase(
            final Class<E> clazz,
            final Object[] toReplace,
            final Predicate<E> filter
    ) {
        super(
                clazz,
                filter
        );
        this.toReplace = toReplace;
    }

    public ReplaceBase(
            final Class<E> clazz,
            final Object[] toReplace,
            final Predicate<E> filter,
            final Change<E> parent
    ) {
        super(
                clazz,
                filter,
                parent
        );
        this.toReplace = toReplace;
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected Change<E> toSequential(Change<E>[] changes) {
        return null;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    public Object[] getToReplace() {
        return toReplace;
    }
}
