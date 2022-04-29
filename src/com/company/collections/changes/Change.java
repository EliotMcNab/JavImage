package com.company.collections.changes;

import com.company.collections.ImmutableCollection;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;

public abstract class Change<E> implements ImmutableCollection<E> {

    // ====================================
    //               FIELDS
    // ====================================

    protected final int size;
    protected final Change<E> parent;
    protected final E[] toAdd;
    protected final Object[] toRemove;
    protected final Predicate<? super E> filter;
    protected final Object[] toRetain;
    protected final E[] array;
    private final int generation;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    protected Change(
            final E[] array
    ) {
        this(
                0,
                array.length,
                null,
                null,
                null,
                null,
                null,
                array
        );
    }

    protected Change(
            final int generation,
            final int size,
            final Change<E> parent,
            final E[] toAdd,
            final Object[] toRemove,
            final Predicate<? super E> filter,
            final Object[] toRetain,
            final E[] array
    ) {
        this.generation = generation;
        this.size = size;
        this.parent = parent;
        this.toAdd = toAdd;
        this.toRemove = toRemove;
        this.filter = filter;
        this.toRetain = toRetain;
        this.array = array;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    public abstract Object[] getChanges();

    // ====================================
    //               ADDING
    // ====================================

    @Override
    public ArrayAdd<E> add(E e) {
        return new ArrayAdd<>(e, this);
    }

    @Override
    public ArrayAdd<E> addAll(Collection<? extends E> c) {
        return new ArrayAdd<>((E[]) c.toArray(), this);
    }

    // ====================================
    //              REMOVING
    // ====================================

    @Override
    public ArrayRemove<E> remove(Object o) {
        return new ArrayRemove<>(o, this);
    }

    public final ArrayRemove<E> removeAll(E... e) {
        return new ArrayRemove<>(e, this);
    }

    @Override
    public ArrayRemove<E> removeAll(Collection<?> c) {
        return new ArrayRemove<>(c.toArray(), this);
    }

    @Override
    public ArrayRemove<E> removeIf(Predicate<? super E> filter) {
        return new ArrayRemove<>(filter, this);
    }

    // ====================================
    //              RETAINING
    // ====================================

    @Override
    public ArrayRetain<E> retain(Object o) {
        return new ArrayRetain<>(List.of(o));
    }

    @Override
    public ArrayRetain<E> retainAll(Object... o) {
        return new ArrayRetain<>(Arrays.asList(o));
    }

    @Override
    public ArrayRetain<E> retainAll(Collection<?> c) {
        return new ArrayRetain<>(c, this);
    }

    // ====================================
    //              APPLYING
    // ====================================

    @SafeVarargs
    public static <E> ArrayBase<E> of(E... e) {
        return new ArrayBase<>(e);
    }

    public final E[] applyTo(Collection<E> c, Class<E> clazz) {
        return applyTo((E[]) c.toArray());
    }
    public final E[] applyTo(E[] array) {
        if (getGeneration() == 0) return applyToImpl(array, (Class<E>) array[0].getClass());
        else                      return resolve(array, (Class<E>) array[0].getClass());
    }

    protected abstract E[] applyToImpl(E[] array, Class<E> clazz);

    protected final E[] resolve(E[] array, Class<E> clazz) {

        // region getting all changes
        int i = 0, k = 0;
        int generation = this.generation;
        Change<E> currentChange = this;

        final Change<E>[] allChanges = new Change[generation + 1];

        while (generation > 0) {
            allChanges[i++] = currentChange;
            currentChange = currentChange.parent;
            generation = currentChange.generation;
        }

        allChanges[i] = currentChange;
        // endregion

        // region applying changes
        E[] result = array;

        
        for (i = 0; i < allChanges.length; i++) {
            // region optimising sequential changes
            while (i+1 < allChanges.length && Objects.equals(allChanges[i].getClass(), allChanges[++i].getClass()));

            final int sequentialLength = i - k;
            if (sequentialLength > 1) {
                final Change<E>[] subArray = (Change<E>[]) Array.newInstance(Change.class, sequentialLength);
                System.arraycopy(allChanges, k, subArray, 0, sequentialLength);
                currentChange = sequential(subArray);
                i--;
            } else {
                currentChange = allChanges[i];
            }
            System.out.println(i);
            // endregion

            result = currentChange.applyToImpl(result, clazz);
            k = i;
        }
        // endregion

        return result;
    }

    @SafeVarargs
    private Change<E> sequential(
            Change<E>... changes
    ) {
        // TODO: make it so Change subclasses are responsible for specifying a sequential implementation
        return switch (changes[0]) {
            case ArrayAdd    add    -> new SequentialAdd<>(changes);
            case ArrayRemove remove -> new SequentialRemove<>(changes);
            case ArrayRetain retain -> new SequentialRetain<>(changes);
            default                 -> throw new IllegalArgumentException("Unhandled change class");
        };
    }

    private boolean isFinal(final Change<E> change) {
        return generation == 0;
    }

    public final int getGeneration() {
        return generation;
    }

    // ====================================
    //              CLEARING
    // ====================================

    @Override
    public Change<E> clear() {
        return new ArrayClear<>();
    }

    // ====================================
    //             CONTENTS
    // ====================================

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0 && filter == null;
    }

    @Override
    public abstract boolean contains(Object o);

    @Override
    public abstract boolean containsAll(Collection<?> c);

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public final Object[] toArray() {
        if (array != null) {
            return applyTo(array);
        } else {
            return new Object[0];
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public abstract String toString();

}
