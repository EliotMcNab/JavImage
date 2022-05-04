package com.company.collections.changes.add;

import com.company.collections.changes.Change;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Add<E> extends AddBase<E> implements Iterable<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public Add(
            final Class<E> clazz,
            final E[] toAdd
    ) {
        super(
                clazz,
                toAdd
        );
    }

    public Add(
            final Class<E> clazz,
            final E[] toAdd,
            final Change<E> parent
    ) {
        super(
                clazz,
                toAdd,
                parent
        );
    }

    public Add(
            final Class<E> clazz,
            final Collection<? extends E> c
    ) {
        super(
                clazz,
                (E[]) c.toArray(),
                null
        );
    }

    public Add(
            final Class<E> clazz,
            final Collection<? extends E> c,
            final Change<E> parent
    ) {
        super(
                clazz,
                (E[]) c.toArray(),
                parent
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected E[] applyToImpl(E[] array) {
        final E[] result = (E[]) Array.newInstance(clazz, array.length + toAdd.length);
        System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(toAdd, 0, result, array.length, toAdd.length);
        return result;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    @Override
    public E[] getChanges() {
        return toAdd;
    }

    // ====================================
    //             CONTENTS
    // ====================================

    @Override
    public boolean contains(Object o) {
        return Arrays.asList(toAdd).contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(Arrays.asList(toAdd)).containsAll(c);
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
        return "Add{toAdd=" +
                Arrays.toString(toAdd) +
                "}";
    }
}
