package com.company.collections.changeAPI.information.get;

import com.company.utilities.ArrayUtil;

/**
 * {@link com.company.collections.changeAPI.information.ChangeInformation ChangeInformation} responsible for retrieving
 * all elements in an array at specific indexes. Can handle multiple indexes
 * @param <E> the type the ChangeInformation operates on
 */
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
