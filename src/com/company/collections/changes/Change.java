package com.company.collections.changes;

import com.company.collections.ImmutableCollection;
import com.company.collections.InaccessibleValueException;
import com.company.collections.changes.add.Add;
import com.company.collections.changes.clear.Clear;
import com.company.collections.changes.remove.RemoveIf;
import com.company.collections.changes.remove.RemoveFirst;
import com.company.collections.changes.remove.RemoveAll;
import com.company.collections.changes.retain.RetainAll;
import com.company.collections.changes.retain.RetainIf;
import com.company.collections.changes.retain.RetainFirst;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;

public abstract class Change<E> implements ImmutableCollection<E> {

    // ====================================
    //               FIELDS
    // ====================================

    protected final Class<E> clazz;
    private final Change<E> parent;
    protected final E[] array;
    private final int generation;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public Change(
            final Class<E> clazz
    ) {
        this.clazz = clazz;
        this.parent = null;
        this.array = null;
        this.generation = 0;
    }

    public Change(
            final Class<E> clazz,
            final Change<E> parent
    ) {
        this.clazz = clazz;
        this.parent = parent;
        this.array = parent == null ? null : parent.array;
        this.generation = parent == null ? 0 : parent.generation + 1;
    }

    public Change(
            final Class<E> clazz,
            final Change<E> parent,
            final E[] array
    ) {
        this.clazz = clazz;
        this.parent = parent;
        this.array = array;
        this.generation = parent == null ? 0 : parent.generation + 1;
    }

    @SafeVarargs
    public static <E> Origin<E> of(final E... elements) {
        return new Origin<>((Class<E>) elements.getClass().componentType(), elements);
    }

    public static <E> Origin<E> of(final Class<E> clazz) {
        return new Origin<>(clazz, (E[]) Array.newInstance(clazz, 0));
    }


    // ====================================
    //               ADDING
    // ====================================

    @Override
    public Add<E> add(E e) {
        return new Add<>(clazz, (E[]) new Object[]{e}, this);
    }

    public Add<E> addAll(E... elements) {
        return new Add<>(clazz, elements, this);
    }

    @Override
    public Add<E> addAll(Collection<? extends E> c) {
        return new Add<>(clazz, c, this);
    }

    // ====================================
    //              REMOVING
    // ====================================

    @Override
    public RemoveFirst<E> removeFirst(Object o) {
        return new RemoveFirst<>(clazz, new Object[]{o}, this);
    }

    public RemoveFirst<E> removeFirst(Object... objects) {
        return new RemoveFirst<>(clazz, objects, this);
    }

    public final RemoveAll<E> removeAll(E... e) {
        return new RemoveAll<>(clazz, e, this);
    }

    @Override
    public RemoveAll<E> removeAll(Collection<?> c) {
        return new RemoveAll<>(clazz, c, this);
    }

    @Override
    public RemoveIf<E> removeIf(Predicate<? super E> filter) {
        return new RemoveIf<>(clazz, filter, this);
    }

    // ====================================
    //              RETAINING
    // ====================================

    @Override
    public RetainFirst<E> retainFirst(Object o) {
        return new RetainFirst<>(clazz, new Object[]{o}, this);
    }

    public RetainFirst<E> retainFirst(Object... objects) {
        return new RetainFirst<>(clazz, objects, this);
    }

    @Override
    public RetainAll<E> retainAll(Object... objects) {
        return new RetainAll<>(clazz, objects, this);
    }

    @Override
    public RetainAll<E> retainAll(Collection<?> c) {
        return new RetainAll<>(clazz, c, this);
    }

    public RetainIf<E> retainIf(Predicate<? super E> filter) {
        return new RetainIf<>(clazz, filter, this);
    }

    // ====================================
    //              CLEARING
    // ====================================

    @Override
    public Change<E> clear() {
        return new Clear<>(clazz);
    }

    // ====================================
    //              APPLYING
    // ====================================

    protected abstract Change<E> toSequential(final Class<E> clazz, Change<E>[] changes);

    public final E[] applyTo(Collection<? extends E> c) {
        return applyTo((E[]) c.toArray());
    }
    public final E[] applyTo(@NotNull E[] array) {
        Objects.requireNonNull(array);

        if (getGeneration() == 0) return applyToImpl(array);
        else                      return resolve(array);
    }

    protected abstract E[] applyToImpl(E[] array);

    protected final E[] resolve(E[] array) {

        // region getting all changes
        int i = 0, k = 0;
        int generation = this.generation;
        Change<E> currentChange = this;

        final Change<E>[] allChanges = new Change[generation + 1];

        while (generation > 0) {
            allChanges [allChanges.length - 1 - i++] = currentChange;
            currentChange = currentChange.parent;
            generation = currentChange.generation;
        }

        allChanges[0] = currentChange;
        // endregion

        // region applying changes
        E[] result = array;

        for (i = 0; i < allChanges.length; i++) {
            // region optimising sequential changes
            // while (i+1 < allChanges.length && Objects.equals(allChanges[i].getClass(), allChanges[i+1].getClass())) {i++;}
            for (; i+1 < allChanges.length && Objects.equals(allChanges[i].getClass().getSuperclass(), allChanges[i+1].getClass().getSuperclass()); i++);

            final int sequentialLength = i - k;
            if (sequentialLength > 1) {
                final Change<E>[] subArray = (Change<E>[]) Array.newInstance(Change.class, sequentialLength);
                System.arraycopy(allChanges, k + 1, subArray, 0, sequentialLength);
                currentChange = subArray[0].toSequential(clazz, subArray);
            } else {
                currentChange = allChanges[i];
            }
            // endregion

            result = currentChange.applyToImpl(result);
            k = i;
        }
        // endregion

        return result;
    }

    private boolean isFinal(final Change<E> change) {
        return generation == 0;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    /**
     * Gets the changes that would occur as a result of calling applyTo or toArray <br>
     * (an empty array signifies either that no change would take place or that changes cannot be determined
     * before calling either applyTo to toArray, as is the case with {@link RetainIf})
     * @return (Object[]): changes resulting from calling applyTo or toArray
     */
    public abstract Object[] getChanges();

    public final int getGeneration() {
        return generation;
    }

    // ====================================
    //             CONTENTS
    // ====================================

    /**
     * Returns the size of the {@link Change} <br>
     * (a size of -1 indicates that the change cannot know in advance the size its impact, for example {@link RemoveIf})
     * @return (int): the amount of elements the change will affect in a given array
     */
    @Override
    public abstract int size();

    @Override
    public abstract boolean isEmpty();

    @Override
    public abstract boolean contains(Object o);

    @Override
    public abstract boolean containsAll(Collection<?> c);

    /**
     * Determines whether the given element would be changed
     * @param o ({@code Object}): the {@link Object} to check
     * @return (boolean): whether the object would be changed
     */
    public abstract boolean matches(final Object o);

    public abstract boolean allMatch(final Object... objects);

    public abstract boolean allMatch(final Collection<Object> c);

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public final E[] toArray() {
        if (array != null) {
            return applyTo(array);
        } else {
            throw new InaccessibleValueException("Can't use toArray, no array was specified to apply changes to");
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public abstract String toString();

}
