package com.company.collections.changeAPI.information.count;

import java.util.function.Predicate;

public class CountIf<E> extends CountBase<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public CountIf(
            final Predicate<E> filter
    ) {
        super(
                new Object[0],
                filter
        );
    }

    // ====================================
    //            INFORMATION
    // ====================================

    @Override
    public Integer getInformation(E[] array) {
        int count = 0;
        for (E e : array) {
            if (filter.test(e)) count++;
        }
        return count;
    }
}
