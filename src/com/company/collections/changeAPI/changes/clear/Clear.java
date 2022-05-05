package com.company.collections.changeAPI.changes.clear;

import com.company.collections.changeAPI.Change;

import java.lang.reflect.Array;

public class Clear<E> extends Change<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public Clear(
            final Class<E> clazz
    ) {
        super(
                clazz,
                null,
                (E[]) Array.newInstance(clazz, 0)
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected Change<E> toSequential(Change<E>[] changes) {
        return new Clear<>(clazz);
    }

    @Override
    protected E[] applyToImpl(E[] array) {
        return (E[]) Array.newInstance(clazz, 0);
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "Clear{}";
    }
}
