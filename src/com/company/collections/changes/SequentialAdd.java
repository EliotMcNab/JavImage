package com.company.collections.changes;

public class SequentialAdd<E> extends ArrayAdd<E> {

    public SequentialAdd(
            final Change<E>... changes
    ) {
        super((E[]) optimiseAdd(changes));
    }

    private static int determineTotalChangeLength(
            final Change<?>... changes
    ) {
        int length = 0;
        for (Change<?> change : changes) {
            length += change.toAdd.length;
        }
        return length;
    }

    private static Object[] optimiseAdd(
            final Change<?>... changes
    ) {
        final int totalLength = determineTotalChangeLength(changes);
        final Object[] result = new Object[totalLength];

        int k = 0;
        for (Change<?> change : changes) {
            System.arraycopy(change.toAdd, 0, result, k, change.toAdd.length);
            k += change.toAdd.length;
        }

        return result;
    }

}
