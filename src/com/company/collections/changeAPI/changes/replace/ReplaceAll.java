package com.company.collections.changeAPI.changes.replace;

import com.company.collections.changeAPI.Change;
import com.company.utilities.ArrayUtil;
import com.company.utilities.comparators.ArrayElementComparator;
import com.company.utilities.comparators.ObjectComparator;

import java.util.Arrays;
import java.util.Comparator;

/**
 * {@link Change} responsible for replacing all given elements in an array with new values. Can replace multiple elements
 * with different values associated to each of them at once.<br><br>
 *
 * This Change is not sequentialisable due to how each {@link ReplaceAll} modifies the values in an array and therefore
 * require looping through it again in case values which have just been modified need to be replaced
 * @param <E> the type the Change operates on
 */
public class ReplaceAll<E> extends ReplaceValues<E> {

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public ReplaceAll(
            final Class<E> clazz,
            final E[] toReplace
    ) {
        super(
                clazz,
                toReplace,
                null
        );
    }

    public ReplaceAll(
            final Class<E> clazz,
            final E[] toReplace,
            final Change<E> parent
    ) {
        super(
                clazz,
                toReplace,
                parent
        );
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

        int i = array.length;       // index in search
        int j = wrapped.length - 1; // index in wrapped

        // while not all values to find have been tested...
        while (j >= 0) {
            i--; // moves on to the next element in the search array

            // compares the current element in the array to the current element to find
            final int comparison = comparator.compare(search[i][0], wrapped[j][0]);

            // if search[i][0] < wrapped[j][0]...
            if (comparison < 0) {
                j--; // moves on to the next element to find
                i++; // compares it to the current element before moving on
            }
            // if search[i][0] == wrapped[j][0]...
            else if (comparison == 0) {
                // replaces the current value in the array
                result[(int) search[i][1]] = (E) wrapped[j][1];
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
        return "ReplaceAll{toReplace=" +
                Arrays.toString(getValuesToReplace()) +
                ", replacing=" +
                Arrays.toString(getReplacingValues()) +
                "}";
    }
}
