package com.company.collections.changeAPI.information.find;

import com.company.utilities.ArrayUtil;

/**
 * {@link com.company.collections.changeAPI.information.ChangeInformation ChangeInformation} responsible for finding the
 * first instances of all given elements in an array. Can handle multiple elements to find
 */
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
