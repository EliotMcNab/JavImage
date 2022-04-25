package com.company.collections.changes;

import com.company.collections.ImmutableCollection;

import java.lang.ref.Cleaner;
import java.util.*;
import java.util.function.Predicate;

public abstract class ArrayChange<E> implements ImmutableCollection<E> {

    // ====================================
    //               FIELDS
    // ====================================

    protected final int size;
    protected final ArrayChange<E> parent;
    protected final E[] toAdd;
    protected final Object[] toRemove;
    protected final Predicate<? super E> filter;
    protected final Object[] toRetain;
    private final int generation;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    protected ArrayChange(
            final int generation,
            final int size,
            final ArrayChange<E> parent,
            final E[] toAdd,
            final Object[] toRemove,
            final Predicate<? super E> filter,
            final Object[] toRetain
    ) {
        this.generation = generation;
        this.size = size;
        this.parent = parent;
        this.toAdd = toAdd;
        this.toRemove = toRemove;
        this.filter = filter;
        this.toRetain = toRetain;
    }

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
    public ArrayChange<E> retainAll(Collection<?> c) {
        return new ArrayRetain<>(c, this);
    }

    // ====================================
    //              APPLYING
    // ====================================

    public abstract E[] applyTo(E[] array);

    protected E[] resolve(E[] array) {
        // TODO: optimise this shit by merging succesive similar changes
        final ArrayChange<E>[] changes = new ArrayChange[generation];

        int necessaryDepth = generation;
        ArrayChange<E> currentChange = this;

        for (int i = generation; i > 0; i--) {
            changes[i - 1] = currentChange;
            necessaryDepth = generation - i;
            if (isFinal(currentChange)) break;
        }

        E[] result = array;
        for (int i = 0; i < necessaryDepth; i++) {
             result = changes[i].applyTo(array);
        }

        return result;
    }

    private boolean isFinal(final ArrayChange<E> change) {
        return generation == 0;
    }

    protected final int getGeneration() {
        return generation;
    }

    // ====================================
    //              CLEARING
    // ====================================

    @Override
    public ArrayChange<E> clear() {
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
    public abstract Object[] toArray();

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public abstract String toString();

    /*// ====================================
    //               FIELDS
    // ====================================

    private final E[] changes;
    private final int startIndex;
    private final int size;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ArrayChange() {
        this.changes = (E[]) new Object[0];
        this.startIndex = 0;
        this.size = 0;
    }

    public ArrayChange(final E e) {
        this.changes = (E[]) new Object[]{e};
        this.startIndex = 0;
        this.size = 1;
    }

    public ArrayChange(final Collection<? extends E> c) {
        this.changes = (E[]) c.toArray();
        this.startIndex = 0;
        this.size = c.size();
    }

    private ArrayChange(final E[] changes, final int startIndex, final int size) {
        this.changes = changes;
        this.startIndex = startIndex;
        this.size = size;
    }

    @Override
    public int size() {
        return size;
    }

    public int getStart() {
        return startIndex;
    }

    public ArrayChange<E> setStart(final int newStart) {
        return new ArrayChange<>(Arrays.copyOf(changes, changes.length), newStart, newStart + changes.length);
    }

    public E get(final int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + " Size: " + size);
        else if (index < startIndex)
            throw new InaccessibleValueException("value at index " + index + " is not saved in change");

        return changes[index - startIndex];
    }

    public boolean hasValueAt(final int index) {
        return index >= startIndex && index <= size;
    }

    public ArrayChange<E> set(final int index, final E val) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + " Size: " + size);
        else if (index < startIndex)
            throw new InaccessibleValueException("value at index " + index + " is not saved in change");

        final E[] change = Arrays.copyOf(this.changes, this.changes.length);
        change[index] = val;

        return new ArrayChange<>(change, startIndex, size);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        final E[] sortedArray = (E[]) Arrays.stream(changes).sorted().toArray();
        return Arrays.binarySearch(sortedArray, o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {

        private int cursor;

        @Override
        public boolean hasNext() {
            return cursor < changes.length;
        }

        @Override
        public E next() {
            return changes[cursor++];
        }
    }

    @Override
    public E[] toArray() {
        return changes;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public ArrayChange<E> add(E e) {
        if (size == Integer.MAX_VALUE)
            throw new ArrayStoreException("Impossible to add new changes, Integer Max Value reached");

        final int length = changes.length;

        final E[] newChanges = (E[]) new Object[length + 1];
        System.arraycopy(changes, 0, newChanges, 0, length);
        newChanges[length] = e;

        return new ArrayChange<>(newChanges, startIndex, size + 1);
    }

    @Override
    public ArrayChange<E> addAll(Collection<? extends E> c) {
        if (size + c.size() >= Integer.MAX_VALUE)
            throw new ArrayStoreException("Impossible to add new changes, Integer Max Value reached");

        final int length = changes.length;

        final E[] newChanges = (E[]) new Object[length + c.size()];
        final E[] cArray = (E[]) c.toArray();

        System.arraycopy(changes, 0, newChanges, 0, length);
        System.arraycopy(cArray, 0, newChanges, length, cArray.length);

        return new ArrayChange<>(newChanges, size, size + c.size());
    }

    @Override
    public ArrayChange<E> remove(Object o) {
        final int deletionIndex = Arrays.binarySearch(changes, o);
        if (deletionIndex < 0) return this;

        final int length = changes.length;

        final E[] newChanges = (E[]) new Object[length - 1];
        if (deletionIndex != 0)
            System.arraycopy(changes, 0, newChanges, 0, deletionIndex);
        if (deletionIndex != length - 1)
            System.arraycopy(changes, deletionIndex + 1, newChanges, deletionIndex, length - 1 - deletionIndex);

        return new ArrayChange<>(newChanges, startIndex, size - 1);
    }

    @Override
    public ArrayChange<E> removeAll(Collection<?> c) {
        if (changes.length == 0) return this;

        final int[] collectionSearch = new int[c.size()];

        int position = 0;
        for (int i = 0; i < changes.length; i++) {
            if (c.contains(changes[i])) collectionSearch[position++] = i;
        }

        final int[] deletionIndexes = Arrays.stream(collectionSearch).filter(value -> value >= 0).toArray();
        final int deletionCount = deletionIndexes.length;
        final int newLength = changes.length - deletionCount;
        final E[] newChanges = (E[]) new Object[newLength];

        int lastDeletion = -1;
        int index = 0;
        for (int deletionIndex : deletionIndexes) {
            final int from = lastDeletion + 1;
            final int length = deletionIndex - (lastDeletion + 1);
            lastDeletion = deletionIndex;
            System.arraycopy(changes, from, newChanges, index, length);
            index += length;
        }

        return new ArrayChange<>(newChanges, startIndex, size - deletionCount);
    }

    @Override
    public ImmutableCollection<E> removeIf(Predicate<? super E> filter) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public ArrayChange<E> retainAll(Collection<?> c) {
        if (c.size() == 0) return this;

        final int[] collectionSearch = new int[c.size()];
        final Object[] cArray = c.toArray();
        for (int i = 0; i < c.size(); i++) {
            collectionSearch[i] = Arrays.binarySearch(changes, cArray[i]);
        }

        final Object[] newChanges = Arrays.stream(collectionSearch).filter(value -> value >= 0)
                .sorted()
                .mapToObj(value -> changes[value])
                .toArray();

        return new ArrayChange<>((E[]) newChanges, startIndex, size - changes.length + newChanges.length);
    }

    @Override
    public ArrayChange<E> clear() {
        return new ArrayChange<>((E[]) new Object[0], startIndex, size - changes.length);
    }

    @SafeVarargs
    public final ArrayChange<E> mergeWith(final ArrayChange<E>... changes) {
        final int targetSize = Arrays.stream(changes).mapToInt(value -> value.size)
                                                    .max()
                                                    .getAsInt();

        ArrayChange<E> merge = this;
        for (ArrayChange<E> change : changes) {
            merge = merge.mergeTo(change);
            if (merge.size == targetSize) break;
        }

        return merge;
    }
    public ArrayChange<E> mergeTo(final ArrayChange<E> b) {
        final int startIndex = Math.min(this.startIndex, b.startIndex);
        final int size = this.size;
        final E[] changes = (E[]) new Object[size - startIndex];

        System.arraycopy(this.changes, 0, changes, this.startIndex - startIndex, this.changes.length);

        final int aStart = this.startIndex;
        final int bStart = b.startIndex;
        final int aStop = aStart + this.changes.length;
        final int bStop = bStart + b.changes.length;

        final boolean startOverlap = bStart >= aStart && bStart <= aStop;
        final boolean stopOverlap = bStop >= aStart && bStop <= aStop;

        final int globalStart = startOverlap ? aStop : bStart;
        final int localStart = startOverlap ? this.changes.length - b.startIndex + this.startIndex : 0;
        final int localStop = Math.min(b.changes.length, this.changes.length - b.startIndex + this.startIndex);

        final boolean intercept = bStart < aStart && bStop > aStop;

        if (intercept) {
            final int deltaStart = aStart - bStart;
            final int deltaStop = bStop - aStop;

            System.arraycopy(b.changes, localStart, changes, globalStart, deltaStart);
            System.arraycopy(b.changes, deltaStart + b.changes.length, changes, globalStart, deltaStop);
        } else {
            System.arraycopy(b.changes, localStart, changes, 0, localStop - localStart);
        }

        return new ArrayChange<>(changes, startIndex, size);
    }

    @Override
    public String toString() {
        return Arrays.toString(changes);
    }*/

}
