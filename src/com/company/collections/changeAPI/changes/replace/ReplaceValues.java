package com.company.collections.changeAPI.changes.replace;

import com.company.collections.changeAPI.Change;
import com.company.utilities.ArrayUtil;

public abstract class ReplaceValues<E> extends ReplaceBase<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ReplaceValues(
            final Class<E> clazz,
            final E[] toReplace
    ) {
        super(
                clazz,
                toReplace,
                null
        );
    }

    public ReplaceValues(
            final Class<E> clazz,
            final E[] toReplace,
            final Change<E> parent
    ) {
        super(
                clazz,
                toReplace,
                null,
                parent
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    public Object[] getValuesToReplace() {
        if (toReplace.length % 2 != 0)
            throw new IllegalArgumentException(
                    "Invalid array of elements to replace, " +
                            "must have equal number of values to replace and replacing values"
            );

        return ArrayUtil.retainAtMultipleIndexes(toReplace, 2, false);
    }

    public Object[] getReplacingValues() {
        if (toReplace.length % 2 != 0)
            throw new IllegalArgumentException(
                    "Invalid array of elements to replace, " +
                            "must have equal number of values to replace and replacing values"
            );

        return ArrayUtil.retainAtMultipleIndexes(toReplace, 2, true);
    }

}
