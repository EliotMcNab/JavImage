package com.company.collections.changes.remove;

import com.company.collections.changes.Change;
import com.company.utilities.ArrayUtil;

import java.lang.reflect.Array;
import java.util.*;

public class RemoveFirst<E> extends RemoveBase<E> implements Iterable<Object> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public RemoveFirst(
            final Class<E> clazz,
            final Object[] toRemove
    ) {
        super(
                clazz,
                toRemove,
                null
        );
    }

    public RemoveFirst(
            final Class<E> clazz,
            final Object[] toRemove,
            final Change<E> parent
    ) {
        super(
                clazz,
                toRemove,
                null,
                parent
        );
    }

    public RemoveFirst(
            final Class<E> clazz,
            final Collection<? extends E> c
    ) {
        super(
                clazz,
                c.toArray(),
                null
        );
    }

    public RemoveFirst(
            final Class<E> clazz,
            final Collection<? extends E> c,
            final Change<E> parent
    ) {
        super(
                clazz,
                c.toArray(),
                null,
                parent
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected E[] applyToImpl(E[] array) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(clazz);

        // determines the index of all the element to remove & sorts them
        final int[] indexes = ArrayUtil.quickFindFirst(array, toRemove);
        Arrays.parallelSort(indexes);

        return ArrayUtil.removeAt(array, indexes);
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    @Override
    public Object[] getChanges() {
        return toRemove;
    }

    // =====================w===============
    //             CONTENTS
    // ====================================


    @Override
    public int size() {
        return toRemove.length;
    }

    @Override
    public boolean contains(Object o) {
        return Arrays.asList(toRemove).contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(Arrays.asList(toRemove)).containsAll(c);
    }

    // ====================================
    //             ITERATION
    // ====================================

    @Override
    public Iterator<Object> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<Object> {

        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < toRemove.length;
        }

        @Override
        public Object next() {
            return toRemove[cursor++];
        }
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "RemoveFirst{toRemove=" +
                Arrays.toString(toRemove) +
                "}";
    }
}
