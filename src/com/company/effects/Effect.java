package com.company.effects;

public interface Effect<T> {
    T applyTo(final T t);
}
