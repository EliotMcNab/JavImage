package com.company.collections.changes;

import com.company.collections.changes.retain.RetainFirst;

public class SequentialRetain<E> /*extends RetainFirst<E>*/ {

    /*public SequentialRetain(
            final Change<E>... changes
    ) {
        super(optimiseRetain(changes));
    }

    private static int determineTotalChangeLength(
            final Change<?>... changes
    ) {
        int length = 0;
        for (Change<?> change : changes) {
            length += change.toRetain.length;
        }
        return length;
    }

    private static Object[] optimiseRetain(
            final Change<?>... changes
    ) {
        final int totalLength = determineTotalChangeLength(changes);
        final Object[] result = new Object[totalLength];

        int k = 0;
        for (Change<?> change : changes) {
            System.arraycopy(change.toRetain, 0, result, k, change.toRetain.length);
            k += change.toRetain.length;
        }

        return result;
    }*/

}
