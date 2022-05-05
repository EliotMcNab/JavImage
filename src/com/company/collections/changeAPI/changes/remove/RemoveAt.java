package com.company.collections.changeAPI.changes.remove;

import com.company.collections.changeAPI.Change;
import com.company.utilities.ArrayUtil;

import java.util.Arrays;

public class RemoveAt<E> extends RemoveBase<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public RemoveAt(
            final Class<E> clazz,
            final int[] indexes
    ) {
        super(
                clazz,
                null,
                null,
                indexes
        );
    }

    public RemoveAt(
            final Class<E> clazz,
            final int[] indexes,
            final Change<E> parent
    ) {
        super(
                clazz,
                null,
                null,
                indexes,
                parent
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected E[] applyToImpl(E[] array) {
        return ArrayUtil.removeAt(array, removalIndexes);
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "RemoveAt{" +
                Arrays.toString(removalIndexes) +
                "}";
    }
}
