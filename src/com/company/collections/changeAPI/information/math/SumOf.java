package com.company.collections.changeAPI.information.math;

import com.company.collections.changeAPI.information.ChangeInformation;

/**
 * {@link ChangeInformation} responsible for summing up the hashcode of every element in an array
 * @param <E> the type the ChangeInformation operates on
 */
public class SumOf<E> extends ChangeInformation<E> {

    // ====================================
    //               FIELDS
    // ====================================

    private final int invert;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public SumOf() {
        this.invert = 1;
    }

    public SumOf(final boolean invert) {
        this.invert = invert ? -1 : 1;
    }

    // ====================================
    //            INFORMATION
    // ====================================

    @Override
    public Object getInformation(E[] array) {
        int sum = 0;
        for (E e : array) {
            sum += invert * e.hashCode();
        }
        return sum;
    }
}
