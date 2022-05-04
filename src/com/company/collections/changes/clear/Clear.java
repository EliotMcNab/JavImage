package com.company.collections.changes.clear;

import com.company.collections.changes.Change;

import java.lang.reflect.Array;
import java.util.Collection;

public class Clear<E> extends Change<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public Clear(
            final Class<E> clazz
    ) {
        super(
                clazz,
                null,
                (E[]) Array.newInstance(clazz, 0)
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected Change<E> toSequential(Class<E> clazz, Change<E>[] changes) {
        return new Clear<>((Class<E>) this.getClass().componentType());
    }

    @Override
    protected E[] applyToImpl(E[] array) {
        return (E[]) Array.newInstance(clazz, 0);
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
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean matches(Object o) {
        return false;
    }

    @Override
    public boolean allMatch(Object... objects) {
        return false;
    }

    @Override
    public boolean allMatch(Collection<Object> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "Clear{}";
    }
}
