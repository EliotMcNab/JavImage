package com.company.collections.changeAPI.changes.retain;

import com.company.collections.changeAPI.Change;
import com.company.collections.changeAPI.changes.remove.SequentialRemoveIf;
import com.company.utilities.ArrayUtil;

import java.util.Arrays;
import java.util.function.Predicate;

public class RetainIf<E> extends RetainBase<E> {

    // ====================================
    //               FIELDS
    // ====================================

    private static final Class<?>[] SEQUENTIALISEABLE = new Class<?>[]{
            RetainIf.class
    };

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
    protected boolean canSequentialise(Change<E> change) {
        return Arrays.asList(SEQUENTIALISEABLE).contains(change.getClass());
    }

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
