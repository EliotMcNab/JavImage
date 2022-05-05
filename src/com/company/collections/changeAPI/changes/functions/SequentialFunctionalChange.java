package com.company.collections.changeAPI.changes.functions;

import com.company.collections.changeAPI.Change;

import java.util.Arrays;
import java.util.function.Function;

public class SequentialFunctionalChange<E> extends FunctionalChange<E> {

    // ====================================
    //               FIELDS
    // ====================================

    private final Change<E>[] changes;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public SequentialFunctionalChange(
            final Class<E> clazz,
            final Change<E>[] changes
    ) {
        super(
                clazz,
                Function.identity()
        );
        this.changes = changes;
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected boolean canSequentialise(Change<E> change) {
        return false;
    }

    @Override
    protected E[] applyToImpl(E[] array) {
        final E[] result = Arrays.copyOf(array, array.length);

        for (int i = 0; i < array.length; i++) {
            for (Change<E> change : changes) {
                final FunctionalChange<E> functionalChange = (FunctionalChange<E>) change;
                result[i] = functionalChange.function.apply(result[i]);
            }
        }

        return result;
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "SequentialFunctionalChange{changes=" +
                Arrays.toString(changes) +
                "}";
    }
}
