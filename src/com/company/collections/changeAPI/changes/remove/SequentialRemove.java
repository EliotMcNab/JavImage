package com.company.collections.changeAPI.changes.remove;

import com.company.collections.changeAPI.Change;
import com.company.utilities.ArrayUtil;
import com.company.utilities.comparators.ArrayElementComparator;
import com.company.utilities.comparators.ObjectComparator;

import java.util.*;

public class SequentialRemove<E> extends RemoveBase<E>{

    // =====================================
    //               FIELDS
    // =====================================

    private final Change<E>[] changes;
    private Object[] allToRemove;
    private boolean concatenated = false;
    private final int totalLength;

    // ==================================
    //            CONSTRUCTOR
    // ==================================

    public SequentialRemove(
            final Class<E> clazz,
            final Change<E>[] changes
    ) {
        super(
                clazz,
                null,
                null,
                new int[0]
        );
        this.changes = changes;
        this.totalLength = determineTotalChangeLength(changes);
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    private int determineTotalChangeLength(
            final Change<E>[] changes
    ) {
        int length = 0;
        for (Change<E> change : changes) {
            final RemoveBase<E> remove = (RemoveBase<E>) change;
            if (remove.toRemove != null) length += remove.toRemove.length;
        }
        return length;
    }

    private Change<E>[][] separateChanges(final Change<E>[] changes) {

        final Change<E>[][] result = new Change[3][];
        final Change<E>[] removeAll = new Change[changes.length];
        final Change<E>[] removeFirst = new Change[changes.length];
        final Change<E>[] removeIf = new Change[changes.length];

        int kAll = 0, kFirst = 0, kIf = 0;

        for (Change<E> change : changes) {
            switch (change) {
                case RemoveAll   e -> removeAll[kAll++] = change;
                case RemoveFirst e -> removeFirst[kFirst++] = change;
                case RemoveIf    e -> removeIf[kIf++] = change;
                default                 -> throw new IllegalArgumentException("Unhandled change class " + change);
            }
        }

        result[0] = Arrays.copyOf(removeAll, kAll);
        result[1] = Arrays.copyOf(removeFirst, kFirst);
        result[2] = Arrays.copyOf(removeIf, kIf);

        return result;
    }

    private Object[] concatenateToRemove(final Change<E>[] changes) {
        int length = 0;
        for (Object o : changes) {
            length += ((RemoveBase<E>) o).toRemove.length;
        }

        final Object[] result = new Object[length];

        int k = 0;
        for (Change<E> change : changes) {
            if (change == null) continue;

            final RemoveBase<E> remove = (RemoveBase<E>) change;
            System.arraycopy(remove.toRemove, 0, result, k, remove.toRemove.length);
            k += remove.toRemove.length;
        }

        return result;
    }

    @Override
    protected E[] applyToImpl(E[] array) {
        // validates the parameters
        if (array.length == 0) return array;

        // Separate changes into different classes
        final Change<E>[][] removalTypes = separateChanges(changes);
        final Change<E>[] removeAll = removalTypes[0];
        final Change<E>[] removeFirst = removalTypes[1];
        final Change<E>[] removeIf = removalTypes[2];

        // concatenates all elements to remove
        final Object[] removeAllValues =
                removeAll.length == 0 ? new Object[0] : ArrayUtil.retainDistinct(concatenateToRemove(removeAll));
        final Object[] removeFirstValues =
                removeFirst.length == 0 ? new Object[0] : ArrayUtil.retainDistinct(concatenateToRemove(removeFirst));

        // creating search array
        final Object[][] a = new Object[][]{{Integer.MIN_VALUE, -1}};
        final Object[][] b = ArrayUtil.wrapArrayIndexes(array);
        final Object[][] search = ArrayUtil.concatenate(a, b);

        // creating result arrays
        final int[] allOccurenceIndexes = new int[array.length];
        final int[] firstOccurenceIndexes = new int[removeFirstValues.length];
        Arrays.fill(firstOccurenceIndexes, Integer.MAX_VALUE);

        // initialing comparator
        final Comparator<Object> comparator = new ObjectComparator();

        // sorting arrays
        Arrays.parallelSort(search, new ArrayElementComparator<>(0));
        Arrays.parallelSort(removeAllValues, comparator);
        Arrays.parallelSort(removeFirstValues, comparator);

        // initialising counters
        int kAll = 0;
        int kFirst = 0;
        int jAll = removeAllValues.length - 1;
        int jFirst = removeFirstValues.length - 1;
        int iSearch = search.length;

        // main loop
        while (jAll >= 0 | jFirst >= 0) {
            iSearch--; // move on to the next value in the search array

            boolean runAgain = false;

            // region ignoring values which are both in removeAllValues and removeFirstValues
            if (jAll >= 0) {
                // while removeAllValues[jAll] == removeFirstValues[jFirst]
                while (jFirst >= 0 && comparator.compare(removeAllValues[jAll], removeFirstValues[jFirst]) == 0) {
                    jFirst--; // moves on to the next element to find the first occurrence of

                    // if previous element had been found, moves on to the next position in the array
                    if (firstOccurenceIndexes[kFirst] != Integer.MAX_VALUE) {
                        kFirst++;
                    }
                }
            }
            // endregion

            // region finding all value instances
            if (jAll >= 0) {
                final int comparison = comparator.compare(search[iSearch][0], removeAllValues[jAll]);

                // search[iSearch][0] < removeAllValues[jAll]
                if (comparison < 0) {
                    jAll--; // moves on to the next element to find all occurrences off
                    runAgain = true;
                }
                // search[iSearch][0] == removeAllValues[jAll]
                else if (comparison == 0) {
                    allOccurenceIndexes[kAll++] = (int) search[iSearch][1];
                }
            }
            // endregion

            // region finding first value instance
            // if jFirst >= 0 && (jAll < 0 || removeAllValues[jAll] < removeFirstValues[jFirst])
            if (jFirst >= 0 && (jAll < 0 || comparator.compare(removeAllValues[jAll], removeFirstValues[jFirst]) < 0)) {
                final int comparison = comparator.compare(search[iSearch][0], removeFirstValues[jFirst]);

                // search[iSearch][0] < removeFirstValues[jFirst]
                if (comparison < 0) {
                    jFirst--; // moves on to the next element to find first occurrences off
                    runAgain = true;

                    // if previous element had been found, moves on to the next position in the array
                    if (firstOccurenceIndexes[kFirst] != Integer.MAX_VALUE) {
                        kFirst++;
                    }
                }
                // search[iSearch][0] == removeFirstValues[jFirst] && search[iSearch][0] < indexFirst[kFirst]
                else if (comparison == 0 && comparator.compare(search[iSearch][1], firstOccurenceIndexes[kFirst]) < 0) {
                    firstOccurenceIndexes[kFirst] = (int) search[iSearch][1];
                }
            }
            // endregion

            if (runAgain) {
                iSearch++;
            }
        }

        // concatenates and sorts all the found indexes
        final int[] allIndexes = ArrayUtil.concatenate(Arrays.copyOf(allOccurenceIndexes, kAll), Arrays.copyOf(firstOccurenceIndexes, kFirst));
        Arrays.parallelSort(allIndexes);

        // returns new array without elements to remove
        return ArrayUtil.removeAt(array, allIndexes);
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "SequentialRemove{changes=" +
                Arrays.toString(changes) +
                "}";
    }
}
