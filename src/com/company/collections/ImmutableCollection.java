package com.company.collections;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface ImmutableCollection<E> extends Iterable<E> {
    int size();

    boolean isEmpty();

    boolean contains(Object o);

    Object[] toArray();

    <T> T[] toArray(T[] a);

    default <T> T[] toArray(IntFunction<T[]> generator) {
        return toArray(generator.apply(0));
    }

    ImmutableCollection<E> add(E e);

    ImmutableCollection<E> remove(Object o);

    boolean containsAll(Collection<?> c);

    ImmutableCollection<E> addAll(Collection<? extends E> c);

    ImmutableCollection<E> removeAll(Collection<?> c);

    ImmutableCollection<E> removeIf(Predicate<? super E> filter);

    ImmutableCollection<E> retain(Object o);

    ImmutableCollection<E> retainAll(Collection<?> c);

    ImmutableCollection<E> retainAll(Object... o);

    ImmutableCollection<E> clear();

    /*default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 0);
    }

    Stream<E> stream();

    Stream<E> parallelStream();*/

}
