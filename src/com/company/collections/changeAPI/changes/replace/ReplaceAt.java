package com.company.collections.changeAPI.changes.replace;

import com.company.collections.changeAPI.Change;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ReplaceAt<E> extends ReplaceBase<E> {

    // ====================================
    //               FIELDS
    // ====================================

    private static final Class<?>[] SEQUENTIALISEABLE = new Class<?>[]{
        ReplaceAt.class
    };

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ReplaceAt(
            final Class<E> clazz,
            final Object[] toReplace
    ) {
        super(
                clazz,
                toReplace,
                null
        );
    }

    public ReplaceAt(
            final Class<E> clazz,
            final Object[] toReplace,
            final Change<E> parent
    ) {
        super(
                clazz,
                toReplace,
                null,
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
    protected Change<E> toSequential(Change<E>[] changes) {
        return null;
    }

    public int[] getIndexes() {
        final int[] result = new int[toReplace.length / 2];

        try {
            for (int i = 0; i < toReplace.length - 1; i += 2) {
                result[i / 2] = (int) toReplace[i];
            }
        } catch (ArrayStoreException e) {
            throw new IllegalArgumentException("Invalid array of elements to replace, even indexes can only contain position of values to replace");
        }

        return result;
    }

    public E[] getValues() {
        final E[] result = (E[]) Array.newInstance(clazz, toReplace.length / 2);

        try {
            for (int i = 1; i < toReplace.length; i += 2) {
                result[(i - 1) / 2] = (E) toReplace[i];
            }
        } catch (ArrayStoreException e) {
            throw new IllegalArgumentException("Invalid array of elements to replace, odd indexes must contain values of class " + clazz);
        }

        return result;
    }

    @Override
    protected E[] applyToImpl(E[] array) {
        if (toReplace.length % 2 != 0)
            throw new IllegalArgumentException("Invalid array of elements to replace, must have equal number of indexes and values");

        final int[] indexes = getIndexes();
        final E[] values = getValues();

        final E[] result = Arrays.copyOf(array, array.length);

        for (int i = 0; i < indexes.length; i++) {
            result[indexes[i]] = values[i];
        }

        return result;
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "ReplaceAt{toReplace=" +
                Arrays.toString(toReplace) +
                "}";
    }
}
