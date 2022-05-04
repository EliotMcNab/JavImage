package com.company.collections.changes.retain;

import com.company.collections.changes.Change;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public abstract class RetainBase<E> extends Change<E> {

    // ====================================
    //               FIELDS
    // ====================================

    protected final Object[] toRetain;
    protected final Predicate<? super E> filter;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public RetainBase(
            final Class<E> clazz,
            final Object[] toRetain,
            final Predicate<? super E> filter
    ) {
        super(clazz);
        this.toRetain = toRetain;
        this.filter = filter;
    }

    public RetainBase(
            final Class<E> clazz,
            final Object[] toRetain,
            final Predicate<? super E> filter,
            final Change<E> parent
    ) {
        super(
                clazz,
                parent
        );
        this.toRetain = toRetain;
        this.filter = filter;
    }

    // ====================================
    //              APPLYING
    // ====================================

    @Override
    protected Change<E> toSequential(Class<E> clazz, Change<E>[] changes) {
        return null;
    }

    // ====================================
    //             CONTENTS
    // ====================================

    @Override
    public boolean isEmpty() {
        return size() == 0 || filter == null;
    }

    @Override
    public boolean matches(Object o) {
        try {
            return contains(o) || filter.test((E) o);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean allMatch(Object... objects) {
        return allMatch(Arrays.asList(objects));
    }

    @Override
    public boolean allMatch(Collection<Object> c) {
        for (Object o : c) {
            if (!matches(o)) return false;
        }
        return true;
    }
}
