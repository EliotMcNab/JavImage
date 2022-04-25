package com.company.collections.changes;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

public class ArrayAdd<E> extends ArrayChange<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ArrayAdd(final E toAdd) {
        this(toAdd, null);
    }

    protected ArrayAdd(final E toAdd, final ArrayChange<E> parent) {
        this((E[]) new Object[]{toAdd}, parent);
    }

    public ArrayAdd(final Collection<? extends E> c) {
        this((E[]) c.toArray());
    }

    protected ArrayAdd(final Collection<? extends E> c, final ArrayChange<E> parent) {
        this((E[]) c.toArray(), parent);
    }

    public ArrayAdd(final E[] toAdd) {
        this(toAdd, null);
    }

    protected ArrayAdd(final E[] toAdd, final ArrayChange<E> parent) {
        super(
                parent == null ? 0 : parent.getGeneration(),
                toAdd.length,
                parent,
                toAdd,
                null,
                null,
                null
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
        for (E e : toAdd) {
            if (o.equals(e)) return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(c)) return false;
        }
        return true;
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
            return cursor < toAdd.length;
        }

        @Override
        public E next() {
            return toAdd[cursor++];
        }
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(toAdd, toAdd.length);
    }

    @Override
    public String toString() {
        return Arrays.toString(toAdd);
    }
}
