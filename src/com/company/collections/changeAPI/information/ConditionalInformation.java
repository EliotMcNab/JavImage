package com.company.collections.changeAPI.information;

import java.util.function.Predicate;

public abstract class ConditionalInformation<E> extends ChangeInformation<E> {

    // ====================================
    //               FIELDS
    // ====================================

    protected final Predicate<E> filter;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ConditionalInformation(
            final Predicate<E> filter
    ) {
        this.filter = filter;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    public Predicate<E> getFilter() {
        return filter;
    }

}
