package com.company.collections.changes;

import com.company.utilities.ArrayUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class ArrayRetain<E> extends Change<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ArrayRetain(final Collection<?> c) {
        this(c.toArray());
    }

    protected ArrayRetain(final Collection<?> c, final Change<E> parent) {
        this(c.toArray(), parent);
    }

    public ArrayRetain(final Object[] toRetain) {
        this(toRetain, null);
    }

    protected ArrayRetain(final Object[] toRetain, final Change<E> parent) {
        super(
                parent == null ? 0 : parent.getGeneration() + 1,
                toRetain.length,
                parent,
                null,
                null,
                null,
                toRetain,
                parent == null ? null : parent.array
        );
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    @Override
    public Object[] getChanges() {
        return toRetain;
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected E[] applyToImpl(E[] array, Class<E> clazz) {
        // TODO: update this shit to match optimised algorithms in ArrayRemove
        // creates an array with all the values to be retained
        final Object[] retained = Arrays.stream(ArrayUtil.quickFindAll(array, toRetain))
                .filter(value -> value >= 0)
                .sorted()
                .distinct()
                .mapToObj(value -> array[value])
                .toArray();

        // creates a new array of the correct type and copies the values to be retained there
        final E[] result = (E[]) Array.newInstance(clazz, retained.length);
        System.arraycopy((E[]) retained, 0, result, 0, retained.length);

        // returns the final result with the correct class associated to it
        return result;
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
    public String toString() {
        return Arrays.toString(toRetain);
    }
}
