package com.company.collections.changeAPI.information.get;

import com.company.utilities.ArrayUtil;

public class GetAt<E> extends GetBase<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public GetAt(
            final int[] indexes
    ) {
        super(
                indexes,
                null
        );
    }

    // ====================================
    //            INFORMATION
    // ====================================

    @Override
    public E[] getInformation(E[] array) {
        return ArrayUtil.retainAt(array, indexes);
    }
}
