package com.company.collections.changeAPI.information.get;

import com.company.utilities.ArrayUtil;

import java.util.function.Predicate;

/**
 * {@link com.company.collections.changeAPI.information.ChangeInformation ChangeInformation} responsible for finding all
 * elements which match a given {@link Predicate}
 * @param <E> the type the ChangeInformation operates on
 */
public class GetAllIf<E> extends GetBase<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public GetAllIf(
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
