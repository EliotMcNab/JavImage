package com.company.collections.changes;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class ArrayRetain<E> extends ArrayChange<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ArrayRetain(final Collection<?> c) {
        this(c.toArray());
    }

    protected ArrayRetain(final Collection<?> c, final ArrayChange<E> parent) {
        this(c.toArray(), parent);
    }

    public ArrayRetain(final Object[] toRetain) {
        this(toRetain, null);
    }

    protected ArrayRetain(final Object[] toRetain, final ArrayChange<E> parent) {
        super(
                parent == null ? 0 : parent.getGeneration(),
                toRetain.length,
                parent,
                null,
                null,
                null,
                toRetain
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    public E[] applyTo(E[] array) {
        return null;
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

        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < toRetain.length;
        }

        @Override
        public E next() {
            return (E) toRetain[cursor++];
        }
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(toRetain, toRetain.length);
    }

    @Override
    public String toString() {
        return Arrays.toString(toRetain);
    }
}
