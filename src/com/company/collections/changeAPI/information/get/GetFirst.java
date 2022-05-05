package com.company.collections.changeAPI.information.get;

import java.util.function.Predicate;

public class GetFirst<E> extends GetBase<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public GetFirst(
            final Predicate<E> filter
    ) {
        super(
                new int[0],
                filter
        );
    }

    // ====================================
    //            INFORMATION
    // ====================================

    @Override
    public E getInformation(E[] array) {
        for (E e : array) {
            if (filter.test(e)) return e;
        }
        return null;
    }
}
