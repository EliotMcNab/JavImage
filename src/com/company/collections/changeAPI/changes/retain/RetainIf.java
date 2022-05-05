package com.company.collections.changeAPI.changes.retain;

import com.company.collections.changeAPI.Change;
import com.company.utilities.ArrayUtil;

import java.util.function.Predicate;

public class RetainIf<E> extends RetainBase<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public RetainIf(
            final Class<E> clazz,
            final Predicate<? super E> filter
    ) {
        super(
                clazz,
                null,
                filter
        );
    }

    public RetainIf(
            final Class<E> clazz,
            final Predicate<? super E> filter,
            final Change<E> parent
    ) {
        super(
                clazz,
                null,
                filter,
                parent
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected E[] applyToImpl(E[] array) {
        return ArrayUtil.retainAt(array, ArrayUtil.findMatches(array, filter));
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "RetainIf{predicate=" +
                filter +
                "}";
    }
}
