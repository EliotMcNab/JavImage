package com.company.collections.changeAPI.information.count;

import com.company.utilities.ArrayUtil;

/**
 * {@link com.company.collections.changeAPI.information.ChangeInformation ChangeInformation} responsible for counting
 * the number of occurrences of certain elements in an array.
 * @param <E> the type the ChangeInformation operates on
 */
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
