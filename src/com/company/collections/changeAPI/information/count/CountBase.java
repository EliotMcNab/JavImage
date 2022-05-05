package com.company.collections.changeAPI.information.count;

import com.company.collections.changeAPI.information.ChangeInformation;
import com.company.collections.changeAPI.information.ConditionalInformation;

import java.util.Arrays;
import java.util.function.Predicate;

public abstract class CountBase<E> extends ConditionalInformation<E> {

    // ====================================
    //               FIELDS
    // ====================================

    protected final Object[] toCount;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public CountBase(
            final Object[] toCount,
            final Predicate<E> filter
    ) {
        super(filter);
        this.toCount = toCount;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    public Object[] getToCount() {
        return Arrays.copyOf(toCount, toCount.length);
    }
}
