package com.company.collections;

import java.util.*;
import java.util.function.Predicate;

public class ListChange<E> implements ImmutableCollection<E> {

/*
    // ====================================
    //               FIELDS
    // ====================================

    private E[] changes;
    private int startIndex;
    private int size;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ListChange() {
        this.changes = (E[]) new Object[0];
        this.startIndex = 0;
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return Arrays.binarySearch(changes, o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {

        private int cursor;

        @Override
        public boolean hasNext() {
            return cursor < size;
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
    public boolean add(E e) {
        if (size == Integer.MAX_VALUE)
            throw new ArrayStoreException("Impossible to add new changes, Integer Max Value reached");

        size++;
        final E[] newChanges = (E[]) new Object[size];
        System.arraycopy(changes, 0, newChanges, 0, size - 1);
        newChanges[size - 1] = e;
        changes = newChanges;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (size + c.size() >= Integer.MAX_VALUE)
            throw new ArrayStoreException("Impossible to add new changes, Integer Max Value reached");

        final E[] newChanges = (E[]) new Object[size + c.size()];
        final E[] cArray = (E[]) c.toArray();

        System.arraycopy(changes, 0, newChanges, 0, size);
        System.arraycopy(cArray, 0, newChanges, size, cArray.length);
        changes = newChanges;
        size += c.size();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        final int deletionIndex = Arrays.binarySearch(changes, o);
        if (deletionIndex < 0) return false;

        final E[] newChanges = (E[]) new Object[size - 1];
        if (deletionIndex != 0)
            System.arraycopy(changes, 0, newChanges, 0, deletionIndex);
        if (deletionIndex != size - 1)
            System.arraycopy(changes, deletionIndex + 1, newChanges, deletionIndex, size - 1 - deletionIndex);
        changes = newChanges;
        size--;

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        final int[] collectionSearch = new int[c.size()];
        final Object[] cArray = c.toArray();
        for (int i = 0; i < c.size(); i++) {
            collectionSearch[i] = Arrays.binarySearch(changes, cArray[i]);
        }

        final int[] deletionIndexes = Arrays.stream(collectionSearch).filter(value -> value >= 0).sorted().toArray();
        final int deletionCount = deletionIndexes.length;
        final int newSize = size - deletionCount;
        final E[] newChanges = (E[]) new Object[newSize];

        int lastDeletion = -1;
        int index = 0;
        for (int deletionIndex : deletionIndexes) {
            final int from = lastDeletion + 1;
            final int length = deletionIndex - (lastDeletion + 1);
            lastDeletion = deletionIndex;
            System.arraycopy(changes, from, newChanges, index, length);
            index += length;
        }

        changes = newChanges;
        size = newChanges.length;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.size() == 0) return false;

        final int[] collectionSearch = new int[c.size()];
        final Object[] cArray = c.toArray();
        for (int i = 0; i < c.size(); i++) {
            collectionSearch[i] = Arrays.binarySearch(changes, cArray[i]);
        }

        final Object[] newChanges = Arrays.stream(collectionSearch).filter(value -> value >= 0)
                                                                   .sorted()
                                                                   .mapToObj(value -> changes[value])
                                                                   .toArray();

        changes = (E[]) newChanges;
        size = newChanges.length;
        return true;
    }

    @Override
    public void clear() {
        changes = (E[]) new Object[0];
        size = 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(changes);
    }
*/

    // ====================================
    //               FIELDS
    // ====================================

    private final E[] changes;
    private final int startIndex;
    private final int size;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ListChange() {
        this.changes = (E[]) new Object[0];
        this.startIndex = 0;
        this.size = 0;
    }

    private ListChange(final E[] changes, final int startIndex, final int size) {
        this.changes = changes;
        this.startIndex = startIndex;
        this.size = size;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return Arrays.binarySearch(changes, o) >= 0;
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
    public ListChange<E> add(E e) {
        if (size == Integer.MAX_VALUE)
            throw new ArrayStoreException("Impossible to add new changes, Integer Max Value reached");

        final int length = changes.length;

        final E[] newChanges = (E[]) new Object[length + 1];
        System.arraycopy(changes, 0, newChanges, 0, length);
        newChanges[length] = e;

        return new ListChange<>(newChanges, startIndex, size + 1);
    }

    @Override
    public ListChange<E> addAll(Collection<? extends E> c) {
        if (size + c.size() >= Integer.MAX_VALUE)
            throw new ArrayStoreException("Impossible to add new changes, Integer Max Value reached");

        final int length = changes.length;

        final E[] newChanges = (E[]) new Object[length + c.size()];
        final E[] cArray = (E[]) c.toArray();

        System.arraycopy(changes, 0, newChanges, 0, length);
        System.arraycopy(cArray, 0, newChanges, length, cArray.length);

        return new ListChange<>(newChanges, startIndex, size + c.size());
    }

    @Override
    public ListChange<E> remove(Object o) {
        final int deletionIndex = Arrays.binarySearch(changes, o);
        if (deletionIndex < 0) return this;

        final int length = changes.length;

        final E[] newChanges = (E[]) new Object[length - 1];
        if (deletionIndex != 0)
            System.arraycopy(changes, 0, newChanges, 0, deletionIndex);
        if (deletionIndex != length - 1)
            System.arraycopy(changes, deletionIndex + 1, newChanges, deletionIndex, length - 1 - deletionIndex);

        return new ListChange<>(newChanges, startIndex, size - 1);
    }

    @Override
    public ListChange<E> removeAll(Collection<?> c) {
        final int[] collectionSearch = new int[c.size()];
        final Object[] cArray = c.toArray();
        for (int i = 0; i < c.size(); i++) {
            collectionSearch[i] = Arrays.binarySearch(changes, cArray[i]);
        }

        final int[] deletionIndexes = Arrays.stream(collectionSearch).filter(value -> value >= 0).sorted().toArray();
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

        return new ListChange<>(newChanges, startIndex, size + newChanges.length);
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
    public ListChange<E> retainAll(Collection<?> c) {
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

        return new ListChange<>((E[]) newChanges, startIndex, size - changes.length + newChanges.length);
    }

    @Override
    public ListChange<E> clear() {
        return new ListChange<>((E[]) new Object[0], startIndex, size - changes.length);
    }

    @Override
    public String toString() {
        return Arrays.toString(changes);
    }
}
