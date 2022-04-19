package com.company.collections;

import java.util.*;

public class ListChange<E> implements Collection<E> {

    // ====================================
    //               FIELDS
    // ====================================

    private final HashMap<Integer, Object> changes;
    private int start;
    private int size;

    // ====================================
    //            CONSTRUCTOR
    // ====================================

    public ListChange() {
        changes = new HashMap<>();
        start = 0;
        size = 0;
    }

    public ListChange(final E[] array) {
        changes = new HashMap<>();
        start = 0;
        size = array.length;
        registerChanges(array);
    }

    public ListChange(final int start, final int size) {
        this.changes = new HashMap<>();
        this.start = start;
        this.size = size;
    }

    private void registerChanges(final Object[] array) {
        for (int i = 0; i < array.length; i++) {
            changes.put(i, array[i]);
        }
    }

    @Override
    public int size() {
        return size - start;
    }

    public Set<Map.Entry<Integer, E>> getChanges() {
        final Set<Map.Entry<Integer, E>> entrySet = new HashSet<>();

        for (Map.Entry<Integer, Object> entry : changes.entrySet()) {
            entrySet.add(new AbstractMap.SimpleEntry<>(entry.getKey(), (E) entry.getValue()));
        }

        return entrySet;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return changes.containsKey(o);
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {

        private int cursor = start;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            return (E) changes.get(cursor++);
        }
    }

    @Override
    public Object[] toArray() {
        return changes.values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        if (size == Integer.MAX_VALUE) throw new ArrayStoreException("maximum number of changes reached");
        changes.put(size++, e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (Map.Entry<Integer, Object> entry : changes.entrySet()) {
            if (Objects.equals(o, entry.getValue())) {
                changes.remove(entry.getKey());
                updateIndexes(entry.getKey());
                size--;
                return true;
            }
        }
        return false;
    }

    private void updateIndexes(final int index) {
        for (int i = index + 1; i < size; i++) {
            replaceKey(i, i - 1);
        }
    }

    private void replaceKey(final int oldKey, final int newKey) {
        if (!changes.containsKey(oldKey)) return;
        changes.put(newKey, changes.get(oldKey));
        changes.remove(oldKey);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return changes.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed = remove(o) || changed;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (Object value : changes.values()) {
            if (!c.contains(value)) {
                remove(value);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        size = 0;
        start = 0;
        changes.clear();
    }

    public E get(final int index) {
        return (E) changes.get(index);
    }

    @Override
    public boolean equals(Object o) {
        return switch (o) {
            case ListChange<?> l -> {
                final boolean sameChanges = compareChanges(l, this);
                final boolean sameStart = l.start == this.start;
                final boolean sameSize = l.size == this.size;
                yield sameChanges && sameStart && sameSize;
            }
            default -> false;
        };
    }

    private boolean compareChanges(final ListChange<?> a, final ListChange<?> b) {
        return Arrays.equals(a.changes.keySet().toArray(), b.changes.keySet().toArray()) &&
               Arrays.equals(a.changes.values().toArray(), b.changes.values().toArray());
    }

    @Override
    public int hashCode() {
        return changes.hashCode() * Integer.hashCode(start) * Integer.hashCode(size);
    }

    @Override
    public String toString() {
        return changes.values().toString();
    }
}
