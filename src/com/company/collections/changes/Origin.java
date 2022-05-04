package com.company.collections.changes;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class Origin<E> extends Change<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public Origin(
            final Class<E> clazz
    ) {
        super(
                clazz,
                null,
                (E[]) new Object[0]
        );
    }

    public Origin(
            final Class<E> clazz,
            final E[] array
    ) {
        super(
                clazz,
                null,
                array
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected Change<E> toSequential(Class<E> clazz, Change<E>[] changes) {
        return null;
    }

    @Override
    protected E[] applyToImpl(E[] array) {
        return array;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    @Override
    public Object[] getChanges() {
        return new Object[0];
    }

    // ====================================
    //             CONTENTS
    // ====================================

    @Override
    public int size() {
        return array.length;
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
        return containsAll(Arrays.asList(objects));
    }

    @Override
    public boolean allMatch(Collection<Object> c) {
        return containsAll(c);
    }

    @Override
    public boolean contains(Object o) {
        return Arrays.asList(array).contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(Arrays.asList(array)).containsAll(c);
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "Origin{array=" +
                Arrays.toString(array) +
                "}";
    }
}
