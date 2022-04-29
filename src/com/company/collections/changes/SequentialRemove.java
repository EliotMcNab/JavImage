package com.company.collections.changes;

public class SequentialRemove<E> extends ArrayRemove<E>{

    // ==================================
    //            CONSTRUCTOR
    // ==================================

    @SafeVarargs
    public SequentialRemove(
            final Change<E>... changes
    ) {
        super(optimiseRemove(changes));
    }

    private static int determineTotalChangeLength(
            final Change<?>... changes
    ) {
        int length = 0;
        for (Change<?> change : changes) {
            length += change.toRemove.length;
        }
        return length;
    }

    private static Object[] optimiseRemove(
            final Change<?>... changes
    ) {
        final int totalLength = determineTotalChangeLength(changes);
        final Object[] result = new Object[totalLength];

        int k = 0;
        for (Change<?> change : changes) {
            System.arraycopy(change.toRemove, 0, result, k, change.toRemove.length);
            k += change.toRemove.length;
        }

        return result;
    }

}
