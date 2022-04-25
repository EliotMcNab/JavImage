package com.company.collections;

import javax.swing.plaf.IconUIResource;
import java.util.*;
import java.util.function.Predicate;

public class ImmutableList<E> implements ImmutableCollection<E> {
    @Override
    public int size() {
        return 0;
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
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public ImmutableCollection<E> add(E e) {
        return null;
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
    public ImmutableCollection<E> addAll(Collection<? extends E> c) {
        return null;
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
    public Iterator<E> iterator() {
        return null;
    }

    /*// ====================================
    //               FIELDS
    // ====================================

    private final int generation;
    private final ImmutableList<E> parent;
    private final ArrayChange<E> change;
    private final int size;

    // ====================================
    //            CONSTRUCTOR
    // ====================================

    public ImmutableList() {
        generation = 0;
        parent = null;
        change = new ArrayChange<>();
        size = 0;
    }

    public ImmutableList(final Collection<? extends E> c) {
        generation = 0;
        parent = null;
        change = new ArrayChange<>(c);
        size = c.size();
    }

    private ImmutableList(final ArrayChange<E> change) {
        this.generation = 0;
        this.parent = null;
        this.change = change;
        this.size = change.size();
    }
    private ImmutableList(final ImmutableList<E> parent, final ArrayChange<E> change) {
        this.generation = parent.generation + 1;
        this.parent = parent;
        this.change = change;
        this.size = change.size();
    }

    @Override
    public int size() {
        return size;
    }

    public int getGeneration() {
        return generation;
    }

    public ImmutableList<E> getParent() {
        return parent;
    }

    public E get(final int index) {
        if (!validateIndex(index)) {
            throw new IndexOutOfBoundsException("Index: " + index + " Size: " + size);
        } if (change.hasValueAt(index)) {
            return change.get(index);
        } else {
            return applyChanges().get(index);
        }
    }

    private boolean validateIndex(final int index) {
        return index >= 0 && index < size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (change.contains(o)) return true;
        else return applyChanges().change.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {

        private final E[] array = (E[]) toArray();
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
    public Object[] toArray() {
        if (generation == 0) {
            return change.toArray();
        } else {
            return applyChanges().change.toArray();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    public ImmutableList<E> applyChanges() {
        ArrayChange<E> newChange = this.change;
        ImmutableList<E> parent = this;
        for (int i = 0; i < generation; i++) {
            parent = parent.parent;
            newChange = newChange.mergeTo(parent.change);
        }
        return new ImmutableList<>(newChange);
    }

    @Override
    public ImmutableList<E> add(E e) {
        return new ImmutableList<>(this, new ArrayChange<>(e).setStart(change.size()));
    }

    @Override
    public ImmutableList<E> addAll(Collection<? extends E> c) {
        return new ImmutableList<>(this, new ArrayChange<E>(c).setStart(change.size()));
    }

    @Override
    public ImmutableList<E> remove(Object o) {
        final ArrayChange<E> newChange = applyChanges().change.remove(o);
        return new ImmutableList<>(this, newChange);
    }

    @Override
    public ImmutableList<E> removeAll(Collection<?> c) {
        if (change.containsAll(c)) return new ImmutableList<>(this, change.removeAll(c));
        else return new ImmutableList<>(this, applyChanges().change.removeAll(c));
    }

    @Override
    public ImmutableList<E> removeIf(Predicate<? super E> filter) {
        // TODO: needs implementation in ArrayChange class
        return null;
    }

    @Override
    public ImmutableList<E> retainAll(Collection<?> c) {
        final ArrayChange<E> newChange = applyChanges().change.retainAll(c);
        return new ImmutableList<>(this, newChange);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (change.containsAll(c)) return true;
        else return applyChanges().change.containsAll(c);
    }

    @Override
    public ImmutableList<E> clear() {
        return new ImmutableList<>(new ArrayChange<>());
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }*/
}
