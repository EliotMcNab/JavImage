package com.company.collections.changeAPI.changes.functions;

import com.company.collections.changeAPI.Change;
import com.company.collections.changeAPI.changes.add.Add;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Function;

/**
 * {@link Change} responsible for applying a {@link Function} to every element in an array
 * @param <E> the type the Change operates on
 */
public class ForEach<E> extends FunctionalChange<E> {

    // ====================================
    //               FIELDS
    // ====================================

    private static final Class<?>[] SEQUENTIALISEABLE = new Class<?>[]{ForEach.class};

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
    protected boolean canSequentialise(Change<E> change) {
        return Arrays.asList(SEQUENTIALISEABLE).contains(change.getClass());
    }

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
