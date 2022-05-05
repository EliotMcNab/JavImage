package com.company.collections.changeAPI;

import com.company.collections.ImmutableCollection;
import com.company.collections.InaccessibleValueException;
import com.company.collections.changeAPI.changes.add.Add;
import com.company.collections.changeAPI.changes.clear.Clear;
import com.company.collections.changeAPI.information.count.CountIf;
import com.company.collections.changeAPI.information.count.CountOccurrences;
import com.company.collections.changeAPI.information.find.FindAll;
import com.company.collections.changeAPI.information.find.FindFirst;
import com.company.collections.changeAPI.changes.functions.ForEach;
import com.company.collections.changeAPI.information.get.GetAll;
import com.company.collections.changeAPI.information.get.GetAt;
import com.company.collections.changeAPI.information.get.GetFirst;
import com.company.collections.changeAPI.changes.ordered.Ordered;
import com.company.collections.changeAPI.changes.remove.RemoveAt;
import com.company.collections.changeAPI.changes.remove.RemoveIf;
import com.company.collections.changeAPI.changes.remove.RemoveFirst;
import com.company.collections.changeAPI.changes.remove.RemoveAll;
import com.company.collections.changeAPI.changes.retain.RetainAll;
import com.company.collections.changeAPI.changes.retain.RetainIf;
import com.company.collections.changeAPI.changes.retain.RetainFirst;
import com.company.collections.changeAPI.changes.unique.Unique;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Change<E> implements ImmutableCollection<E>, Iterable<Change<E>> {

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
    public final Add<E> add(E e) {
        return new Add<>(clazz, (E[]) new Object[]{e}, this);
    }

    public final Add<E> addAll(E... elements) {
        return new Add<>(clazz, elements, this);
    }

    @Override
    public final Add<E> addAll(Collection<? extends E> c) {
        return new Add<>(clazz, c, this);
    }

    // ====================================
    //              REMOVING
    // ====================================

    @Override
    public final RemoveFirst<E> removeFirst(Object o) {
        return new RemoveFirst<>(clazz, new Object[]{o}, this);
    }

    public final RemoveFirst<E> removeFirst(Object... objects) {
        return new RemoveFirst<>(clazz, objects, this);
    }

    public final RemoveAll<E> removeAll(E... e) {
        return new RemoveAll<>(clazz, e, this);
    }

    @Override
    public final RemoveAll<E> removeAll(Collection<?> c) {
        return new RemoveAll<>(clazz, c, this);
    }

    @Override
    public final RemoveIf<E> removeIf(Predicate<? super E> filter) {
        return new RemoveIf<>(clazz, filter, this);
    }

    public final RemoveAt<E> removeAt(int... indexes) {
        return new RemoveAt<>(clazz, indexes, this);
    }

    // ====================================
    //              RETAINING
    // ====================================

    @Override
    public final RetainFirst<E> retainFirst(Object o) {
        return new RetainFirst<>(clazz, new Object[]{o}, this);
    }

    public final RetainFirst<E> retainFirst(Object... objects) {
        return new RetainFirst<>(clazz, objects, this);
    }

    @Override
    public final RetainAll<E> retainAll(Object... objects) {
        return new RetainAll<>(clazz, objects, this);
    }

    @Override
    public final RetainAll<E> retainAll(Collection<?> c) {
        return new RetainAll<>(clazz, c, this);
    }

    public final RetainIf<E> retainIf(Predicate<? super E> filter) {
        return new RetainIf<>(clazz, filter, this);
    }

    // ====================================
    //              CLEARING
    // ====================================

    @Override
    public final Clear<E> clear() {
        return new Clear<>(clazz);
    }

    // ====================================
    //              SORTING
    // ====================================

    public final Ordered<E> ordered() {
        return new Ordered<>(clazz, this);
    }

    public final Ordered<E> ordered(final Comparator<E> comparator) {
        return new Ordered<>(clazz, comparator, this);
    }

    // ====================================
    //               UNIQUE
    // ====================================

    public final Unique<E> unique() {
        return new Unique<>(clazz, this);
    }

    public final Unique<E> unique(final Comparator<E> comparator) {
        return new Unique<>(clazz, comparator, this);
    }

    // ====================================
    //             FUNCTIONS
    // ====================================

    public final ForEach<E> forEach(final Function<E, E> function) {
        return new ForEach<>(clazz, function, this);
    }

    // ====================================
    //            INFORMATION
    // ====================================

    public final int[] findFirst(Object... toFind) {
        return new FindFirst(toFind).getInformation(toArray());
    }

    public final int[] findAll(Object... toFind) {
        return new FindAll(toFind).getInformation(toArray());
    }

    public final E[] getAt(int... indexes) {
        return new GetAt<E>(indexes).getInformation(toArray());
    }

    public final E getFirst(final Predicate<E> filter) {
        return new GetFirst<>(filter).getInformation(toArray());
    }

    public final E[] getAll(final Predicate<E> filter) {
        return new GetAll<>(filter).getInformation(toArray());
    }

    public int[] countMatches(final Object... toFind) {
        return new CountOccurrences<E>(toFind).getInformation(toArray());
    }

    public int countMatches(final Predicate<E> filter) {
        return new CountIf<>(filter).getInformation(toArray());
    }

    // ====================================
    //              APPLYING
    // ====================================

    protected abstract Change<E> toSequential(Change<E>[] changes);

    public final Origin<E> optimise() {
        return new Origin<>(clazz, toArray());
    }

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

        int i = 0, k = 0;
         final Change<E>[] allChanges = retrieveAllChanges(this);

        // region applying changes
        E[] result = array;

        for (; i < allChanges.length; i++) {
            // region optimising sequential changes
            // while (i+1 < allChanges.length && Objects.equals(allChanges[i].getClass(), allChanges[i+1].getClass())) {i++;}
            for (; i+1 < allChanges.length && Objects.equals(allChanges[i].getClass().getSuperclass(), allChanges[i+1].getClass().getSuperclass()); i++);

            final Change<E> currentChange;
            final int sequentialLength = i - k;
            if (sequentialLength > 1) {
                final Change<E>[] subArray = (Change<E>[]) Array.newInstance(Change.class, sequentialLength);
                System.arraycopy(allChanges, k + 1, subArray, 0, sequentialLength);
                currentChange = subArray[0].toSequential(subArray);
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

    private Change<E>[] retrieveAllChanges(final Change<E> change) {
        int i = 0;
        int generation = change.generation;
        Change<E> currentChange = change;

        final Change<E>[] allChanges = new Change[generation + 1];

        while (generation > 0) {
            allChanges [allChanges.length - 1 - i++] = currentChange;
            currentChange = currentChange.parent;
            generation = currentChange.generation;
        }

        allChanges[0] = currentChange;

        return allChanges;
    }

    private boolean isFinal(final Change<E> change) {
        return generation == 0;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    public final int getGeneration() {
        return generation;
    }

    // ====================================
    //             CONTENTS
    // ====================================

    /**
     * Returns the size of the {@link Change} <br>
     * (a size of -1 indicates that the change cannot know in advance the size its impact, for example {@link RemoveIf})
     *
     * @return (int): the amount of elements the change will affect in a given array
     */
    @Override
    public final int size() {
        if (array == null) {
            return 0;
        } else {
            return toArray().length;
        }
    }

    @Override
    public final boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public final boolean contains(Object o) {
        if (array == null) {
            return false;
        } else {
            return Arrays.asList(toArray()).contains(o);
        }
    }

    @Override
    public final boolean containsAll(Collection<?> c) {
        if (array == null) {
            return false;
        } else {
            return new HashSet<>(Arrays.asList(toArray())).containsAll(c);
        }
    }

    // ====================================
    //             ITERATION
    // ====================================

    @NotNull
    @Override
    public Iterator<Change<E>> iterator() {
        return new Itr(this);
    }

    private class Itr implements Iterator<Change<E>> {

        private final Change<E>[] changes;
        private int cursor = 0;

        public Itr(
                final Change<E> change
        ) {
            this.changes = retrieveAllChanges(change);
        }

        @Override
        public boolean hasNext() {
            return cursor < changes.length;
        }

        @Override
        public Change<E> next() {
            return changes[cursor++];
        }
    }


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
