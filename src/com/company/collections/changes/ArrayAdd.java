package com.company.collections.changes;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class ArrayAdd<E> extends Change<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ArrayAdd(final E toAdd) {
        this(toAdd, null);
    }

    protected ArrayAdd(final E toAdd, final Change<E> parent) {
        this((E[]) new Object[]{toAdd}, parent);
    }

    public ArrayAdd(final Collection<? extends E> c) {
        this((E[]) c.toArray());
    }

    protected ArrayAdd(final Collection<? extends E> c, final Change<E> parent) {
        this((E[]) c.toArray(), parent);
    }

    public ArrayAdd(final E[] toAdd) {
        this(toAdd, null);
    }

    protected ArrayAdd(final E[] toAdd, final Change<E> parent) {
        super(
                parent == null ? 0 : parent.getGeneration() + 1,
                toAdd.length,
                parent,
                toAdd,
                null,
                null,
                null,
                parent == null ? null : parent.array
        );
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    @Override
    public E[] getChanges() {
        return toAdd;
    }


    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected E[] applyToImpl(E[] array, Class<E> clazz) {
        final E[] result = (E[]) Array.newInstance(clazz, array.length + toAdd.length);
        System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(toAdd, 0, result, array.length, toAdd.length);
        return result;
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
    public String toString() {
        return Arrays.toString(toAdd);
    }
}
