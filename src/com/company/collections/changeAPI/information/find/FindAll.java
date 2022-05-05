package com.company.collections.changeAPI.information.find;

import com.company.utilities.ArrayUtil;

import java.util.Arrays;

public class FindAll extends FindBase {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public FindAll(
            final Object[] toFind
    ) {
        super(toFind);
    }

    // ====================================
    //            INFORMATION
    // ====================================

    @Override
    public int[] getInformation(Object[] array) {
        final int[] indexes = ArrayUtil.quickFindAll(array, toFind);
        Arrays.parallelSort(indexes);
        return indexes;
    }
}
