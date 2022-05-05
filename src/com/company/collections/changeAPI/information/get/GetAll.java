package com.company.collections.changeAPI.information.get;

import com.company.utilities.ArrayUtil;

import java.util.function.Predicate;

public class GetAll<E> extends GetBase<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public GetAll(
            final Predicate<E> filter
    ) {
        super(
                new int[0],
                filter
        );
    }

    // ====================================
    //            INFORMATION
    // ====================================

    @Override
    public E[] getInformation(E[] array) {
        return ArrayUtil.retainAt(array, ArrayUtil.findMatches(array, filter));
    }
}
