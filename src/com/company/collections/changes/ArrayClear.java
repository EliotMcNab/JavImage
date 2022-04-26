package com.company.collections.changes;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

public class ArrayClear<E> extends ArrayChange<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    protected ArrayClear() {
        super(
                0,
                0,
                null,
                null,
                null,
                null,
                null
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    public E[] applyTo(E[] array, Class<E> clazz) {
        return (E[]) Array.newInstance(clazz, 0);
    }

    // ====================================
    //             CONTENTS
    // ====================================

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    // ====================================
    //             ITERATION
    // ====================================

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            return null;
        }
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public String toString() {
        return "[]";
    }
}
