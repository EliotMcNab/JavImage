package com.company.collections.changes.retain;

import com.company.collections.changes.Change;
import com.company.utilities.ArrayUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;

public class RetainAll<E> extends RetainBase<E> implements Iterable<Object> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public RetainAll(
            final Class<E> clazz,
            final Object[] toRetain
    ) {
        super(
                clazz,
                toRetain,
                null
        );
    }

    public RetainAll(
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

    public RetainAll(
            final Class<E> clazz,
            final Collection<?> c
    ) {
        super(
                clazz,
                c.toArray(),
                null
        );
    }

    public RetainAll(
            final Class<E> clazz,
            final Collection<?> c,
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
        // TODO: make sure that this works
        Objects.requireNonNull(array);
        Objects.requireNonNull(clazz);

        // determines the index of all the element to retain & sorts them
        final int[] indexes = ArrayUtil.quickFindAll(array, toRetain);
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

    // =====================w===============
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
        return new HashSet<>(Arrays.asList(toRetain)).containsAll(c);
    }

    // ====================================
    //             ITERATION
    // ====================================

    @NotNull
    @Override
    public Iterator<Object> iterator() {
        return null;
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
        return "RetainAll{toRetain=" +
                Arrays.toString(toRetain) +
                "}";
    }
}
