package com.company.collections.changeAPI;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * {@link Change} used as a base for further modifications. Only stores an initial array and does not result in any
 * change when applied to an array through toArray or applyTo
 * @param <E> the type the Change operates on
 */
public class Origin<E> extends Change<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public Origin(
            @NotNull final Class<E> clazz
    ) {
        super(
                clazz,
                null,
                (E[]) new Object[0]
        );
    }

    public Origin(
            @NotNull final Class<E> clazz,
            @NotNull final E[] array
    ) {
        super(
                clazz,
                null,
                array
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected boolean canSequentialise(Change<E> change) {
        return false;
    }

    @Override
    protected Change<E> toSequential(Change<E>[] changes) {
        return null;
    }

    @Override
    protected E[] applyToImpl(E[] array) {
        return array;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    public E[] getArray() {
        return Arrays.copyOf(array, array.length);
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "Origin{array=" +
                Arrays.toString(array) +
                "}";
    }
}
