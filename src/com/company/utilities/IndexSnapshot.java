package com.company.utilities;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class IndexSnapshot<T> implements Comparable<IndexSnapshot<T>>{

    // ===================================
    //              FIELDS
    // ===================================

    private final T value;
    private final int oldIndex;

    // ===================================
    //            CONSTRUCTOR
    // ===================================

    public IndexSnapshot(
            final T value,
            final int oldIndex
    ) {
        this.value = value;
        this.oldIndex = oldIndex;
    }

    // ===================================
    //             ACCESSORS
    // ===================================

    public T getValue() {
        return value;
    }

    public int restoreIndex() {
        return oldIndex;
    }

    // ===================================
    //             COMPARING
    // ===================================

    @Override
    public int compareTo(@NotNull IndexSnapshot<T> o) {
        try {
            return ((Comparable<T>) value).compareTo(o.value);
        } catch (ClassCastException e) {
            return Integer.compare(value.hashCode(), o.hashCode());
        }
    }

    // ===================================
    //              DISPLAY
    // ===================================

    @Override
    public String toString() {
        return value.toString();
    }
}
