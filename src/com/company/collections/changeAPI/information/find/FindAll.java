package com.company.collections.changeAPI.information.find;

import com.company.utilities.ArrayUtil;

import java.util.Arrays;

/**
 * {@link com.company.collections.changeAPI.information.ChangeInformation ChangeInformation} responsible for finding
 * the indexes of all given values in an array.
 */
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
