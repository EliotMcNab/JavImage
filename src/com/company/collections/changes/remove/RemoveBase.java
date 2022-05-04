package com.company.collections.changes.remove;

import com.company.collections.changes.Change;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public abstract class RemoveBase<E> extends Change<E> {

    // ====================================
    //               FIELDS
    // ====================================

    // TODO: temporary, before finding proper array size to removal size ratio for big change
    protected static final int BIG_THRESHOLD = 700;

    protected final Object[] toRemove;
    protected final Predicate<? super E> filter;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public RemoveBase(
            final Class<E> clazz,
            final Object[] toRemove,
            final Predicate<? super E> filter
    ) {
        super(clazz);
        this.toRemove = toRemove;
        this.filter = filter;
    }

    public RemoveBase(
            final Class<E> clazz,
            final Object[] toRemove,
            final Predicate<? super E> filter,
            final Change<E> parent
    ) {
        super(
                clazz,
                parent
        );
        this.toRemove = toRemove;
        this.filter = filter;
    }

    // ====================================
    //              APPLYING
    // ====================================

    @Override
    protected Change<E> toSequential(Class<E> clazz, Change<E>[] changes) {
        return new SequentialRemove<>(clazz, changes);
    }

    public boolean isBigChange(final E[] array) {
        return toRemove.length > BIG_THRESHOLD;
    }

    // ====================================
    //             CONTENTS
    // ====================================

    @Override
    public boolean isEmpty() {
        return size() == 0 && filter == null;
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
