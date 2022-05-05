package com.company.collections.changeAPI.changes.remove;

import com.company.collections.changeAPI.Change;
import com.company.utilities.ArrayUtil;

import java.util.*;

public class RemoveFirst<E> extends RemoveBase<E> {

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

    public RemoveFirst(
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

    public RemoveFirst(
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

    public RemoveFirst(
            final Class<E> clazz,
            final Collection<? extends E> c
    ) {
        super(
                clazz,
                c.toArray(),
                null,
                new int[0]
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

        // determines the index of all the element to remove & sorts them
        final int[] indexes = ArrayUtil.quickFindFirst(array, toRemove);
        Arrays.parallelSort(indexes);

        return ArrayUtil.removeAt(array, indexes);
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
