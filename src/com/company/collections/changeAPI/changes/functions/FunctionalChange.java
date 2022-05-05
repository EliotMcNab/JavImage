package com.company.collections.changeAPI.changes.functions;

import com.company.collections.changeAPI.Change;

import java.util.function.Function;

public abstract class FunctionalChange<E> extends Change<E> {

    // ====================================
    //               FIELDS
    // ====================================

    protected final Function<E, E> function;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public FunctionalChange(
            final Class<E> clazz,
            final Function<E, E> function
    ) {
        super(clazz);
        this.function = function;
    }

    public FunctionalChange(
            final Class<E> clazz,
            final Function<E, E> function,
            final Change<E> parent
    ) {
        super(
                clazz,
                parent
        );
        this.function = function;
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected Change<E> toSequential(Change<E>[] changes) {
        return new SequentialFunctionalChange<>(clazz, changes);
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    public Function<E, E> getFunction() {
        return function;
    }
}
