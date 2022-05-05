package com.company.collections.changeAPI.changes.add;

import com.company.collections.changeAPI.Change;

import java.util.Arrays;

public abstract class AddBase<E> extends Change<E> {

    // ====================================
    //               FIELDS
    // ====================================

    protected final E[] toAdd;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public AddBase(
            final Class<E> clazz,
            final E[] toAdd
    ) {
        super(clazz);
        this.toAdd = toAdd;
    }

    public AddBase(
            final Class<E> clazz,
            final E[] toAdd,
            final Change<E> parent
    ) {
        super(
                clazz,
                parent
        );
        this.toAdd = toAdd;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    public E[] getToAdd() {
        return Arrays.copyOf(toAdd, toAdd.length);
    }

    // ====================================
    //              APPLYING
    // ====================================

    @Override
    protected Change<E> toSequential(Change<E>[] changes) {
        return new SequentialAdd<>(clazz, changes);
    }
}
