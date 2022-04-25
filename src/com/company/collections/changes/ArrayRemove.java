package com.company.collections.changes;

import com.company.utilities.ArrayUtil;
import com.sun.security.jgss.GSSUtil;

import javax.management.ObjectName;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

public class ArrayRemove<E> extends ArrayChange<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ArrayRemove(final Collection<? extends E> c) {
        this(c.toArray(), null);
    }

    protected ArrayRemove(final Object toRemove, final ArrayChange<E> parent) {
        this(new Object[]{toRemove}, parent);
    }

    protected ArrayRemove(final Collection<? extends E> c, final ArrayChange<E> parent) {
        this(c.toArray(), parent);
    }

    public ArrayRemove(final Object[] toRemove) {
        this(toRemove, null);
    }

    protected ArrayRemove(final Object[] toRemove, final ArrayChange<E> parent) {
        super(
                parent == null ? 0 : parent.getGeneration(),
                toRemove.length,
                parent,
                null,
                toRemove,
                null,
                null
        );
    }

    public ArrayRemove(final Predicate<? super E> filter) {
        this(filter, null);
    }

    protected ArrayRemove(final Predicate<? super E> filter, final ArrayChange<E> parent) {
        super(
                parent == null ? 0 : parent.getGeneration(),
                0,
                parent,
                null,
                null,
                filter,
                null
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    public E[] applyTo(E[] array) {
        if (size == 1) return simpleRemove(array);
        else           return fastRemove(array);
    }

    private E[] simpleRemove(E[] array) {
        // determines the index of the element to remove
        final int index = ArrayUtil.quickFind(array, toRemove[0]);
        // if element is not in array return a copy of the original array
        if (index == -1) return Arrays.copyOf(array, array.length);

        // copies all  values in original array except for removed value
        final E[] result = (E[]) new Object[array.length - 1];
        if (index != 0) System.arraycopy(array, 0, result, 0, index);
        System.arraycopy(array, index + 1, result, index, array.length - index - 1);

        // returns final result (original array without element to remove)
        return result;
    }

    private E[] fastRemove(E[] array) {
        // determines the index of every element to remove
        final int[] searchIndexes = ArrayUtil.quickFind(array, toRemove);

        // filters out elements which are not contained in the array
        // ignoring duplicate elements
        final int[] containedIndex = Arrays.stream(searchIndexes)
                                           .parallel()
                                           .filter(value -> value >= 0)
                                           .sorted()
                                           .distinct()
                                           .toArray();

        final E[] result = (E[]) new Object[array.length - containedIndex.length];
        int lastRemove = 0;
        int lastIndex = 0;

        if (containedIndex.length == 0) return result;

        // loops through the indexes of the elements contained in the array
        for (int i = 0; i < containedIndex.length; i++) {
            // copies all the elements between the current element and the last
            final int length = containedIndex[i] - lastRemove;
            System.arraycopy(array, lastRemove, result, lastIndex, length);
            lastIndex += length;
            lastRemove = containedIndex[i] + 1;
        }

        // copies over any remaining elements
        final int lastContainedIndex = containedIndex[containedIndex.length - 1];
        if (lastContainedIndex != array.length - 1) {
            System.arraycopy(array, lastRemove, result, lastIndex, array.length - 1 - lastContainedIndex);
        }

        // returns the final resulting array
        return result;
    }

    // ====================================
    //             CONTENTS
    // ====================================

    @Override
    public boolean contains(Object o) {
        for (Object object : toRemove) {
            if (o.equals(object)) return true;
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
            return cursor < toRemove.length;
        }

        @Override
        public E next() {
            return (E) toRemove[cursor++];
        }
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(toRemove, toRemove.length);
    }

    @Override
    public String toString() {
        return Arrays.toString(toRemove);
    }
}
