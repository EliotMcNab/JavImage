package com.company.collections.changeAPI.changes.functions;

import com.company.collections.changeAPI.Change;

import java.lang.reflect.Array;
import java.util.function.Function;

public class ForEach<E> extends FunctionalChange<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ForEach(
            final Class<E> clazz,
            final Function<E, E> function
    ) {
        super(
                clazz,
                function
        );
    }

    public ForEach(
            final Class<E> clazz,
            final Function<E, E> function,
            final Change<E> parent
    ) {
        super(
                clazz,
                function,
                parent
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected E[] applyToImpl(E[] array) {
        final E[] result = (E[]) Array.newInstance(clazz, array.length);

        for (int i = 0; i < array.length; i++) {
            result[i] = function.apply(array[i]);
        }

        return result;
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "ForEach{" +
                function +
                "}";
    }
}
