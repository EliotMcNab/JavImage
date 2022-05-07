package com.company.collections.changeAPI.changes.replace;

import com.company.collections.changeAPI.Change;
import com.company.utilities.ArrayUtil;
import com.company.utilities.comparators.ArrayElementComparator;

import java.lang.reflect.Array;
import java.util.Arrays;

// TODO: make it so replacements are applied consecutively so that replacements can themselves be replaced if they match
//  a new value to replace
/**
 * Sequential implementation of {@link ReplaceAt} which ignores duplicate values to replace with a priority for new
 * replacements (so if a value is replaced multiple times, only its last replacement will take place)
 * @param <E> the type the {@link Change} operates on
 */
public class SequentialReplaceAt<E> extends ReplaceBase<E> {

    // ====================================
    //               FIELDS
    // ====================================

    private final Change<E>[] changes;

    // ====================================
    //             CONSTRUCTOR
    // ========w============================

    public SequentialReplaceAt(
            final Class<E> clazz,
            final Change<E>[] changes
            ) {
        super(
                clazz,
                null,
                null
        );
        this.changes = changes;
    }

    // ====================================
    //          APPLYING CHANGES
    // ====================================

    @Override
    protected boolean canSequentialise(Change<E> change) {
        return false;
    }

    /**
     * Calculates the total amount of elements to replace over all {@link Change Changes}
     * @param changes ({@code Change<E>[]}): changes used to determine the total number of replacements
     * @return (int): total amount of replacements across all the specified changes
     */
    private int determineReplaceCount(final Change<E>[] changes) {
        int totalLength = 0;
        // iterates through every change
        for (Change<E> change : changes) {
            final ReplaceBase<E> replace = (ReplaceBase<E>) change;
            // takes into account its length
            totalLength += replace.toReplace.length / 2;
        }
        // returns the final length
        return totalLength;
    }

    /**
     * Concatenates the replacement indexes in all the specified {@link Change Changes}
     * @param changes ({@code Change<E>[]}): Changes from which to get the indexes
     * @param totalLength (int): total amount of replacements across all specified changes
     * @return (int[]): all indexes at which to perform a replacement
     */
    private int[] concatenateIndexes(final Change<E>[] changes, final int totalLength) {
        final int[] result = new int[totalLength];

        // iterates through every change
        int k = 0;
        for (int i = changes.length - 1; i >= 0; i--) {
            // gets the change's indexes
            final ReplaceAt<E> replace = (ReplaceAt<E>) changes[i];
            final int[] indexes = replace.getIndexes();
            // copies the indexes over to the result array
            System.arraycopy(indexes, 0, result, k, indexes.length);
            k += indexes.length;
        }

        // returns the final array
        return result;
    }

    /**
     * Concatenates the replacement values in all the specified {@link Change Changes}
     * @param changes ({@code Change<E>[]}): Changes from which to get the values
     * @param totalLength (int): total amount of replacements across all specified changes
     * @return (E[]): all replacement values
     */
    private E[] concatenateValues(final Change<E>[] changes, final int totalLength) {
        final E[] result = (E[]) Array.newInstance(clazz, totalLength);

        // iterates through every change
        int k = 0;
        for (int i = changes.length - 1; i >= 0; i--) {
            // gets the change's values
            final ReplaceAt<E> replace = (ReplaceAt<E>) changes[i];
            final E[] indexes = replace.getValues();
            // copies the values over to the result array
            System.arraycopy(indexes, 0, result, k, indexes.length);
            k += indexes.length;
        }

        // returns the final array
        return result;
    }

    /**
     * Maps the specified indexes to their values inside a 2D array<br>
     * <list>
     *     <li>0: (int) index</li>
     *     <li>1: (E) value</li>
     * </list>
     * @param indexes ({@code int[]}): indexes at which to perform the replacements
     * @param values ({@code E[]}): values used as replacements at the specified indexes
     * @return (Object[][]): 2D array mapping indexes to values
     */
    private Object[][] combine(final int[] indexes, final E[] values) {
        final Object[][] result = new Object[indexes.length][2];

        for (int i = 0; i < indexes.length; i++) {
            result[i] = new Object[]{indexes[i], values[i]};
        }

        return result;
    }

    @Override
    protected E[] applyToImpl(E[] array) {
        // determines the total count of replacements
        final int totalLength = determineReplaceCount(changes);

        // gets all indexes at which to replace values
        final int[] indexes = concatenateIndexes(changes, totalLength);
        // gets all replacing values
        final E[] values = concatenateValues(changes, totalLength);
        // maps each replacing value to its corresponding index
        final Object[][] combined = combine(indexes, values);

        // retains only unique indexes with a priority for the first index to be found
        // (since the indexes of the last changes are always first in the array of indexes,
        // this guarantees that the latest changes are always prioritised)
        final Object[][] unique = ArrayUtil.retainDistinct(combined, new ArrayElementComparator<>(0));

        final E[] result = Arrays.copyOf(array, array.length);

        // replaces the values at the specified indexes
        for (Object[] o : unique) {
            // gets the index
            final int index = (int) o[0];
            // gets the value
            final E value = (E) o[1];
            // if the index is valid, replaces the value
            if (index >= 0) result[index] = value;
        }

        // returns the final result
        return result;
    }

    // ====================================
    //          ARRAY CONVERSION
    // ====================================

    @Override
    public String toString() {
        return "SequentialReplaceAt{changes=" +
                Arrays.toString(changes) +
                "}";
    }
}
