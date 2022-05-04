package com.company.collections.changes.remove;

import com.company.collections.changes.Change;
import com.company.utilities.ArrayUtil;

import java.util.Collection;
import java.util.function.Predicate;

public class RemoveIf<E> extends RemoveBase<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public RemoveIf(
            final Class<E> clazz,
            final Predicate<? super E> filter
    ) {
        super(
                clazz,
                null,
                filter
        );
    }

    public RemoveIf(
            final Class<E> clazz,
            final Predicate<? super E> filter,
            final Change<E> parent
    ) {
        super(
                clazz,
                null,
                filter,
                parent
        );
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected E[] applyToImpl(E[] array) {
        return ArrayUtil.removeAt(array, ArrayUtil.findMatches(array, filter));
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    @Override
    public Object[] getChanges() {
        return new Object[0];
    }

    public Predicate<? super E> getFilter() {
        return filter;
    }

    // ====================================
    //             CONTENTS
    // ====================================

    @Override
    public int size() {
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return filter.equals(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!filter.equals(o)) return false;
        }
        return true;
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "RemoveIf{predicate=" +
                filter +
                "}";
    }
}
