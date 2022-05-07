package com.company.collections.changeAPI.information.get;

import java.util.function.Predicate;

/**
 * {@link com.company.collections.changeAPI.information.ChangeInformation ChangeInformation} responsible for retrieving
 * the first element in an array to match a given {@link Predicate}
 * @param <E> the type the ChangeInformation operates on
 */
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
