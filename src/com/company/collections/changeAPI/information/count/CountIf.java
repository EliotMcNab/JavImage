package com.company.collections.changeAPI.information.count;

import java.util.function.Predicate;

/**
 * {@link com.company.collections.changeAPI.information.ChangeInformation ChangeInformation} responsible for counting
 * the number of elements in an array which match a given {@link Predicate}
 * @param <E> the type the ChangeInformation operates on
 */
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
