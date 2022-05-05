package com.company.collections.changeAPI.information.count;

import com.company.utilities.ArrayUtil;

public class CountOccurrences<E> extends CountBase<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public CountOccurrences(
            final Object[] toCount
    ) {
        super(
                toCount,
                null
        );
    }

    // ====================================
    //            INFORMATION
    // ====================================

    @Override
    public int[] getInformation(E[] array) {
        final int[] result = new int[toCount.length];

        for (int i = 0; i < toCount.length; i++) {
            result[i] = ArrayUtil.quickFindAll(array, toCount[i]).length;
        }

        return result;
    }
}
