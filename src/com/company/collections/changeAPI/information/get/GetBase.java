package com.company.collections.changeAPI.information.get;

import com.company.collections.changeAPI.information.ConditionalInformation;

import java.util.Arrays;
import java.util.function.Predicate;

public abstract class GetBase<E> extends ConditionalInformation<E> {

    // ====================================
    //               FIELDS
    // ====================================

    protected final int[] indexes;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public GetBase(
            final int[] indexes,
            final Predicate<E> filter
    ) {
        super(filter);
        this.indexes = indexes;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    public int[] getIndexes() {
        return Arrays.copyOf(indexes, indexes.length);
    }
}
