package com.company.collections.changeAPI.changes.remove;

import com.company.collections.changeAPI.Change;
import com.company.utilities.ArrayUtil;

import java.util.*;

/**
 * {@link Change} responsible for removing all instance of every given element from an array. Allows the removal of
 * multiple elements at once.
 * @param <E> the type the Change operates on
 */
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

        if (isBigChange(array)) return bigRemove(array);
        else                    return smallRemove(array);
    }

    private E[] smallRemove(E[] array) {
        return ArrayUtil.batchRemove(array, clazz, Arrays.asList(toRemove));
    }

    private E[] bigRemove(E[] array) {
        // determines the index of every element to remove & sorts them
        final int[] containedIndex = ArrayUtil.quickFindAll(array, toRemove);;
        Arrays.parallelSort(containedIndex);

        // returns the array without the elements to remove
        return ArrayUtil.removeAt(array, containedIndex);
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
