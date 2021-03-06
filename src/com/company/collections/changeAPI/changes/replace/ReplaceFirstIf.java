package com.company.collections.changeAPI.changes.replace;

import com.company.collections.changeAPI.Change;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * {@link Change} responsible for replacing the first element in an array which matches the given {@link Predicate} with
 * a new value
 * @param <E> the type the Change operates on
 */
public class ReplaceFirstIf<E> extends ReplaceBase<E> {

    // ====================================
    //               FIELDS
    // ====================================

    private static final Class<?>[] SEQUENTIALISEABLE = new Class<?>[]{
            ReplaceFirstIf.class
    };

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ReplaceFirstIf(
            final Class<E> clazz,
            final Predicate<E> filter,
            final E replacingValue
            ) {
        super(
                clazz,
                new Object[]{replacingValue},
                filter
        );
    }

    public ReplaceFirstIf(
            final Class<E> clazz,
            final Predicate<E> filter,
            final E replacingValue,
            final Change<E> parent
    ) {
        super(
                clazz,
                new Object[]{replacingValue},
                filter,
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
    protected Change<E> toSequential(Change<E>[] changes) {
        return new SequentialReplaceFirstIf<>(clazz, changes);
    }

    @Override
    protected E[] applyToImpl(E[] array) {
        final E[] result = Arrays.copyOf(array, array.length);

        for (int i = 0; i < array.length; i++) {
            if (filter.test(array[i])) {
                result[i] = (E) toReplace[0];
                break;
            }
        }

        return result;
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "ReplaceFirstIf{filter=" +
                filter +
                ", replacingValue=" +
                toReplace[0] +
                "}";
    }
}
