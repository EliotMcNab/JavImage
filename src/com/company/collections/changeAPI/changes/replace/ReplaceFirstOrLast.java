package com.company.collections.changeAPI.changes.replace;

import com.company.collections.changeAPI.Change;
import com.company.utilities.ArrayUtil;
import com.company.utilities.comparators.ArrayElementComparator;
import com.company.utilities.comparators.ObjectComparator;

import java.util.Arrays;
import java.util.Comparator;

/**
 * {@link Change} responsible for replacing the first or last instance of a given element in an array with a new value
 * @param <E> the type the Change operates on
 */
public class ReplaceFirstOrLast<E> extends ReplaceValues<E> {

    private final boolean replaceLast;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ReplaceFirstOrLast(
            final Class<E> clazz,
            final E[] toReplace
    ) {
        super(
                clazz,
                toReplace,
                null
        );
        this.replaceLast = false;
    }

    public ReplaceFirstOrLast(
            final Class<E> clazz,
            final E[] toReplace,
            final Change<E> parent
    ) {
        super(
                clazz,
                toReplace,
                parent
        );
        this.replaceLast = false;
    }

    public ReplaceFirstOrLast(
            final Class<E> clazz,
            final E[] toReplace,
            final boolean replaceLast
    ) {
        super(
                clazz,
                toReplace
        );
        this.replaceLast = replaceLast;
    }

    public ReplaceFirstOrLast(
            final Class<E> clazz,
            final E[] toReplace,
            final boolean replaceLast,
            final Change<E> parent
    ) {
        super(
                clazz,
                toReplace,
                parent
        );
        this.replaceLast = replaceLast;
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected boolean canSequentialise(Change<E> change) {
        return false;
    }

    @Override
    protected Change<E> toSequential(Change<E>[] changes) {
        return null;
    }

    @Override
    protected E[] applyToImpl(E[] array) {
        // comparator used for sorting wrapped arrays
        final Comparator<Object[]> sortComparator = new ArrayElementComparator<>(0);

        // maps the values to replace to their replacing values & sorts them
        final Object[][] wrapped = ArrayUtil.wrapArrays(getValuesToReplace(), getReplacingValues());
        Arrays.parallelSort(wrapped, sortComparator);

        // maps the arrays values to their indexes and sorts them
        final Object[][] a = new Object[][]{{Integer.MIN_VALUE, -1}};
        final Object[][] b = ArrayUtil.wrapArrayIndexes(array);
        final Object[][] search = ArrayUtil.concatenate(a, b);
        Arrays.parallelSort(search, sortComparator);

        // creates the result array
        final E[] result = Arrays.copyOf(array, array.length);

        // used to compare elements in the array
        final Comparator<Object> comparator = new ObjectComparator();

        // default value for edgeIndex
        final int bound = replaceLast ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        int i = array.length;       // index in search
        int j = wrapped.length - 1; // index in wrapped
        int edgeIndex = bound;              // smallest or largest index found so far

        // while not all values to find have been tested...
        while (j >= 0) {
            i--; // moves on to the next element in the search array

            // compares the current element in the array to the current element to find
            final int comparison = comparator.compare(search[i][0], wrapped[j][0]);
            // gets the current index in the search array
            final int searchIndex = (int) search[i][1];

            // if search[i][0] < wrapped[j][0]...
            if (comparison < 0) {
                // if the current value was found...
                if (edgeIndex != bound) {
                    result[edgeIndex] = (E) wrapped[j][1]; // replaces it at its first occurrence in the result array
                    edgeIndex = bound;             // resets the edgeIndex
                }
                j--; // moves on to the next element to find
                i++; // compares it to the current element before moving on
            } else if (comparison == 0 && searchIndex < edgeIndex != replaceLast) {
                edgeIndex = searchIndex;
            }
        }

        // returns the final result
        return result;
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "ReplaceFirstOrLast{toReplace=" +
                Arrays.toString(getValuesToReplace()) +
                ", replacing=" +
                Arrays.toString(getReplacingValues()) +
                "}";
    }
}
