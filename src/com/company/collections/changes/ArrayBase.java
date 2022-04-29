package com.company.collections.changes;

import com.company.utilities.ArrayUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class ArrayBase<E> extends Change<E> {

    public ArrayBase(
            final E[] array
    ) {
        super(array);
    }

    @Override
    public Object[] getChanges() {
        return array;
    }

    @Override
    protected E[] applyToImpl(E[] array, Class<E> clazz) {
        return array;
    }

    @Override
    public boolean contains(Object o) {
        return ArrayUtil.quickFind(array, o) > 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(Arrays.asList(array)).containsAll(c);
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {

        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < array.length;
        }

        @Override
        public E next() {
            return array[cursor++];
        }
    }
}
