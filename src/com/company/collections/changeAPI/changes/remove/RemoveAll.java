package com.company.collections.changeAPI.changes.remove;

import com.company.collections.changeAPI.Change;
import com.company.utilities.ArrayUtil;

import java.lang.reflect.Array;
import java.util.*;

public class RemoveAll<E> extends RemoveBase<E> {

    // ====================================
    //               FIELDS
    // ====================================

    private static final Class<?>[] SEQUENTIALISEABLE = new Class<?>[]{
            RemoveAll.class,
            RemoveFirst.class
    };

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public RemoveAll(
            final Class<E> clazz,
            final Object[] toRemove
    ) {
        super(
                clazz,
                toRemove,
                null,
                new int[0]
        );
    }

    public RemoveAll(
            final Class<E> clazz,
            final Object[] toRemove,
            final Change<E> parent
            ) {
        super(
                clazz,
                toRemove,
                null,
                new int[0],
                parent
        );
    }

    public RemoveAll(
            final Class<E> clazz,
            final Collection<?> collection
    ) {
        super(
                clazz,
                collection.toArray(),
                null,
                new int[0]
        );
    }

    public RemoveAll(
            final Class<E> clazz,
            final Collection<?> collection,
            final Change<E> parent
    ) {
        super(
                clazz,
                collection.toArray(),
                null,
                new int[0],
                parent
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected boolean canSequentialise(Change<E> change) {
        return Arrays.asList(SEQUENTIALISEABLE).contains(change.getClass());
    }

    @Override
    protected E[] applyToImpl(E[] array) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(clazz);

        if (isBigChange(array)) return bigRemove(array, clazz);
        else                    return smallRemove(array, clazz);
    }

    private E[] smallRemove(E[] array, Class<E> clazz) {
        return ArrayUtil.batchRemove(array, clazz, Arrays.asList(toRemove));
    }

    private E[] bigRemove(E[] array, final Class<E> clazz) {
        // determines the index of every element to remove
        final int[] searchIndexes = ArrayUtil.quickFindAll(array, toRemove);

        // filters out elements which are not contained in the array
        // ignoring duplicate elements
        final int[] containedIndex = Arrays.stream(searchIndexes)
                .filter(value -> value >= 0)
                .sorted()
                .distinct()
                .toArray();

        final E[] result = (E[]) Array.newInstance(clazz, array.length - containedIndex.length);
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
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "RemoveAll{toRemove=" +
                Arrays.toString(toRemove) +
                "}";
    }
}
