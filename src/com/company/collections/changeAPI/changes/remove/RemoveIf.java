package com.company.collections.changeAPI.changes.remove;

import com.company.collections.changeAPI.Change;
import com.company.utilities.ArrayUtil;

import java.util.function.Predicate;

public class RemoveIf<E> extends RemoveBase<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public RemoveIf(
            final Class<E> clazz,
            final Predicate<? super E> filter
    ) {
        super(
                clazz,
                null,
                filter,
                new int[0]
        );
    }

    public RemoveIf(
            final Class<E> clazz,
            final Predicate<? super E> filter,
            final Change<E> parent
    ) {
        super(
                clazz,
                null,
                filter,
                new int[0],
                parent
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected E[] applyToImpl(E[] array) {
        return ArrayUtil.removeAt(array, ArrayUtil.findMatches(array, filter));
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "RemoveIf{predicate=" +
                filter +
                "}";
    }
}
