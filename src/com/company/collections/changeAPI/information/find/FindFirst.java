package com.company.collections.changeAPI.information.find;

import com.company.utilities.ArrayUtil;

public class FindFirst extends FindBase {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public FindFirst(
            final Object[] toFind
    ) {
        super(toFind);
    }

    // ====================================
    //            INFORMATION
    // ====================================

    @Override
    public int[] getInformation(Object[] array) {
        return ArrayUtil.quickFindFirst(array, toFind);
    }
}
