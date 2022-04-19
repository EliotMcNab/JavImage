package com.company.collections;

import java.util.*;
import java.util.function.Predicate;

public class ImmutableList<E> implements ImmutableCollection<E> {

    // ====================================
    //               FIELDS
    // ====================================

    private final int generation;
    private final ImmutableList<E> parent;
    private final ListChange<E> change;
    private final int size;

    // ====================================
    //            CONSTRUCTOR
    // ====================================

    public ImmutableList() {
        generation = 0;
        parent = null;
        change = new ListChange<>();
        size = 0;
    }

    public ImmutableList(final E[] e) {
        generation = 0;
        parent = null;
        change = new ListChange<>(e);
        size = e.length;
    }

    private ImmutableList(final ImmutableList<E> parent, final ListChange<E> change) {
        this.generation = parent.generation + 1;
        this.parent = parent;
        this.change = change;
        this.size = parent.size() + 1;
    }

    @Override
    public int size() {
        return size;
    }

    public int getGeneration() {
        return generation;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr<>();
    }

    private class Itr<T> implements Iterator<E> {

        private final E[] array = toArray();
        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            return array[cursor++];
        }
    }

    @Override
    public E[] toArray() {
        if (generation == 0) {
            return (E[]) change.toArray();
        } else {
            final E[] array = (E[]) new Object[size];
            ImmutableList<E> currentList = this;

            registerChanges(array, currentList);

            for (int i = 0; i < generation; i++) {
                currentList = currentList.parent;
                registerChanges(array, currentList);
            }

            return array;
        }
    }

    private void registerChanges(final E[] array, final ImmutableList<E> list) {
        for (Map.Entry<Integer, E> entry : list.change.getChanges()) {
            if (array[entry.getKey()] == null) array[entry.getKey()] = entry.getValue();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public ImmutableList<E> add(E e) {
        final ListChange<E> newChange = new ListChange<>(size, size);
        newChange.add(e);
        return new ImmutableList<>(this, newChange);
    }

    @Override
    public ImmutableCollection<E> addAll(Collection<? extends E> c) {
        final ListChange<E> newChange = new ListChange<>(size, size);
        newChange.addAll(c);
        return new ImmutableList<>(this, newChange);
    }

    @Override
    public ImmutableCollection<E> remove(Object o) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public ImmutableCollection<E> removeAll(Collection<?> c) {
        return null;
    }

    @Override
    public ImmutableCollection<E> removeIf(Predicate<? super E> filter) {
        return null;
    }

    @Override
    public ImmutableCollection<E> retainAll(Collection<?> c) {
        return null;
    }

    @Override
    public ImmutableCollection<E> clear() {
        return null;
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    /*

    // =====================================
    //               FIELDS
    // =====================================

    private final int size;
    private final Object[][] compartmentalisedData;
    private static final Object[][] EMPTY_COMPARTMENT_DATA = {};
    private static final int COMPARTMENT_SIZE = 32;

    // =====================================
    //             CONSTRUCTORS
    // =====================================

    public ImmutableList() {
        this.size = 0;
        this.compartmentalisedData = EMPTY_COMPARTMENT_DATA;
    }

    public ImmutableList(final int initialCapacity) {
        if (initialCapacity > 0) {
            this.size = initialCapacity;

            final int compartments = initialCapacity / COMPARTMENT_SIZE + 1;
            this.compartmentalisedData = new Object[compartments][COMPARTMENT_SIZE];
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
    }

    public ImmutableList(final Collection<? extends T> collection) {
        final Object[] collectionArray = collection.toArray();
        final int arrayLength = collectionArray.length;

        if (arrayLength != 0) {
            this.size = collection.size();

            final int compartments = arrayLength / COMPARTMENT_SIZE + 1;
            this.compartmentalisedData = new Object[compartments][COMPARTMENT_SIZE];

            for (int i = 0; i < arrayLength; i+=COMPARTMENT_SIZE) {
                final int currentCompartment = i / COMPARTMENT_SIZE;
                final int to = i + COMPARTMENT_SIZE > arrayLength ? arrayLength : 1 + COMPARTMENT_SIZE;
                this.compartmentalisedData[currentCompartment] = Arrays.copyOfRange(collectionArray, i, to);
            }

        } else {
            this.size = 0;
            this.compartmentalisedData = EMPTY_COMPARTMENT_DATA;
        }
    }

    private ImmutableList(final Object[][] compartmentalisedData, final int size) {
        this.compartmentalisedData = compartmentalisedData;
        this.size = size;
    }

    // =====================================
    //             ITERATION
    // =====================================

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<T> {
        private int cursor;

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public T next() {
            final int i = cursor++;
            if (i > size) {
                throw new NoSuchElementException();
            } else {
                final int compartment = i / COMPARTMENT_SIZE;
                return (T) compartmentalisedData[compartment][i % 32];
            }
        }
    }

    @Override
    public Object[] toArray() {
        final Object[] array = new Object[size];

        if (size != 0) {
            for (int i = 0; i < size; i += COMPARTMENT_SIZE) {
                final int compartmentIndex = i / COMPARTMENT_SIZE;
                final Object[] currentCompartment = compartmentalisedData[compartmentIndex];
                final int destinationPos = Math.min(size, compartmentIndex * COMPARTMENT_SIZE);
                final int length = i + destinationPos > size ? size % COMPARTMENT_SIZE : COMPARTMENT_SIZE;

                System.arraycopy(currentCompartment, 0, array, destinationPos, length);
            }
        }

        return array;
    }

    // =====================================
    //            ARRAY CONVERSION
    // =====================================

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return null;
    }

    // =====================================
    //          ADDING / REMOVING
    // =====================================

    private void smartCopy(
            final Object[][] source,
            Object[][] target,
            int modificationLocation
    ) {
        if (source.length > target.length) {
            throw new IllegalArgumentException(
                    "Illegal Source Length, must be inferior or equal to target length: " + source.length
            );
        }

        if (modificationLocation >= source.length) modificationLocation = source.length - 1;

        if (modificationLocation != 0) {
            System.arraycopy(source, 0, target, 0, modificationLocation);
        }
        final Object[] compartment = source[modificationLocation];
        System.arraycopy(compartment, 0, target[modificationLocation], 0, COMPARTMENT_SIZE);
        System.arraycopy(source, modificationLocation + 1, target, modificationLocation + 1, source.length - modificationLocation - 1);
    }

    public ImmutableList<T> add(T e) {
        final Object[][] newCompartmentalisedData;
        // gets the memory position in the last compartment
        final int compartmentPosition = size % COMPARTMENT_SIZE;
        // determines if there is still space left in the last compartment
        final boolean isFull = compartmentPosition == 0;
        // gets the number of compartments
        final int compartmentsCount = compartmentalisedData.length;
        // gets the current compartment
        final int newCompartmentsCount = isFull ? compartmentsCount + 1 : compartmentsCount;

        // initialises the new compartments
        newCompartmentalisedData = new Object[newCompartmentsCount][COMPARTMENT_SIZE];
        // copies old compartments
        // System.arraycopy(compartmentalisedData, 0, newCompartmentalisedData, 0, compartmentsCount);
        smartCopy(compartmentalisedData, newCompartmentalisedData, newCompartmentsCount);

        // adds the new element to the last compartment
        newCompartmentalisedData[newCompartmentsCount-1][compartmentPosition] = e;

        return new ImmutableList<>(newCompartmentalisedData, size + 1);
    }

    @Override
    public ImmutableList<T> addAll(Collection<? extends T> c) {
        return null;
    }

    public ImmutableList<T> replace(final int index, final T val) {
        Objects.checkIndex(index, size);

        final Object[][] newCompartmentalisedData;
        // gets the number of compartments
        final int compartmentCount = compartmentalisedData.length;
        final int modificationCompartment = index / COMPARTMENT_SIZE;
        final int localModificationIndex = index % COMPARTMENT_SIZE;

        // initialises the new compartments
        newCompartmentalisedData = new Object[compartmentCount][COMPARTMENT_SIZE];
        // smartCopy(compartmentalisedData, newCompartmentalisedData, compartmentCount);

        newCompartmentalisedData[modificationCompartment][localModificationIndex] = val;

        return new ImmutableList<>(newCompartmentalisedData, size);
    }

    @Override
    public ImmutableList<T> remove(Object o) {
        return null;
    }

    @Override
    public ImmutableList<T> removeAll(Collection<?> c) {
        return null;
    }

    @Override
    public ImmutableCollection<T> removeIf(Predicate<? super T> filter) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public ImmutableList<T> retainAll(Collection<?> c) {
        return null;
    }

    @Override
    public ImmutableList<T> clear() {
        return new ImmutableList<>(EMPTY_COMPARTMENT_DATA, 0);
    }

    // =====================================
    //               CONTENTS
    // =====================================

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return compartmentalisedData.equals(EMPTY_COMPARTMENT_DATA);
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    public T get(final int index) {
        Objects.checkIndex(index, size);
        final int currentCompartment = index / COMPARTMENT_SIZE;
        final int indexInCompartment = index % COMPARTMENT_SIZE;
        return (T) compartmentalisedData[currentCompartment][indexInCompartment];
    }

    // =====================================
    //               STRING
    // =====================================

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }
*/
}
