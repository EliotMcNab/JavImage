package com.company.collections.changes.retain;

import com.company.collections.changes.Change;
import com.company.utilities.ArrayUtil;

import java.lang.reflect.Array;
import java.util.*;

public class RetainFirst<E> extends RetainBase<E> implements Iterable<Object> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public RetainFirst(
            final Class<E> clazz,
            final Object[] toRetain
    ) {
        super(
                clazz,
                toRetain,
                null
        );
    }

    public RetainFirst(
            final Class<E> clazz,
            final Object[] toRetain,
            final Change<E> parent
    ) {
        super(
                clazz,
                toRetain,
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

        // determines the index of all the element to retain & sorts them
        final int[] indexes = ArrayUtil.quickFindFirst(array, toRetain);
        Arrays.parallelSort(indexes);

        return ArrayUtil.retainAt(array, indexes);
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    @Override
    public Object[] getChanges() {
        return toRetain;
    }

    // ====================================
    //             CONTENTS
    // ====================================

    @Override
    public int size() {
        return toRetain.length;
    }

    @Override
    public boolean contains(Object o) {
        return Arrays.asList(toRetain).contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(c).containsAll(c);
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
            return cursor < toRetain.length;
        }

        @Override
        public Object next() {
            return toRetain[cursor++];
        }
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "RetainFirst{toRetain=" +
                Arrays.toString(toRetain) +
                "}";
    }
}
