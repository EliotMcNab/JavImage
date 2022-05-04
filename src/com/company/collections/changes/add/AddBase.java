package com.company.collections.changes.add;

import com.company.collections.changes.Change;

import java.util.Arrays;
import java.util.Collection;

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
    //              APPLYING
    // ====================================

    @Override
    protected Change<E> toSequential(Class<E> clazz, Change<E>[] changes) {
        return new SequentialAdd<>(clazz, changes);
    }

    // ====================================
    //             CONTENTS
    // ====================================

    @Override
    public int size() {
        return toAdd.length;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean matches(Object o) {
        return contains(o);
    }

    @Override
    public boolean allMatch(Object... objects) {
        return allMatch(Arrays.asList(objects));
    }

    @Override
    public boolean allMatch(Collection<Object> c) {
        return containsAll(c);
    }
}
