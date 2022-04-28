package com.company.utilities;

import com.company.utilities.comparators.ArrayElementComparator;
import com.company.utilities.comparators.ObjectComparator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;

public class ArrayUtil {
    @Contract(mutates = "param1")
    public static void swap(Object[] array, final int a, final int b) {
        final Object temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    /**
     * Keeps only the distinct elements in an array
     * @param array ({@code T[]}): array to check the values of
     * @return (T[]): distinct elements in the array
     * @param <T> class of the array
     */
    public static <T> T[] retainDistinct(
            @NotNull final T[] array
    ) {
        final Object[][] wrapped = wrapArrayIndexes(array);
        final Object[] distinct = retainDistinctImpl(wrapped, new ArrayElementComparator(0));
        final Object[] result = new Object[distinct.length];

        final Object[][] distinct2D = new Object[distinct.length][2];
        for (int i = 0; i < distinct2D.length; i++) {
            distinct2D[i] = (Object[]) distinct[i];
        }

        Arrays.sort(distinct2D, new ArrayElementComparator(1));

        int k = 0;
        for (int i = 0; i < distinct.length; i++) {
            result[k++] = distinct2D[i][0];
        }

        return (T[]) result;
    }

    /**
     * Sorts and only keeps the distinct elements in an array
     * @param array ({@code T[]}): array to check the values of
     * @param comparator ({@code Comparator\u003C T \u003E}): used to compare the elements in the array
     * @return (T[]): distinct elements in array
     * @param <T> class of the array
     */
    @Contract(mutates = "param1")
    public static <T> T[] retainDistinctImpl(
            @NotNull final T[] array,
            @NotNull final Comparator<T> comparator
    ) {
        final T[] arrayCopy = Arrays.copyOf(array, array.length);
        final Object[] blindResult = new Object[array.length];

        Arrays.sort(arrayCopy, comparator);
        blindResult[0] = arrayCopy[0];

        int i, k;
        for (i = 1, k = 1; i < arrayCopy.length; i++) {
            if (comparator.compare(arrayCopy[i], arrayCopy[i-1]) != 0) blindResult[k++] = arrayCopy[i];
        }

        return (T[]) Arrays.copyOf(blindResult, k);
    }

    /**
     * Concatenates multiple arrays together
     * @param clazz ({@code Class\u003C T \u003E}): array element's class
     * @param arrays ({@code T[]...}): arrays to concatenate
     * @return resulting array concatenation
     * @param <T> class of the array
     */
    @SafeVarargs
    public static <T> T[] concatenate(
            @NotNull final Class<T> clazz,
            @NotNull final T[]... arrays
    ) {
        int totalLength = 0;
        for (T[] array : arrays) {
            totalLength += array.length;
        }

        final T[] result = (T[]) Array.newInstance(clazz, totalLength);

        int k = 0;
        for (T[] array : arrays) {
            System.arraycopy(array, 0, result, k, array.length);
            k += array.length;
        }

        return result;
    }

    /**
     * Finds the first occurrence of a value in an array
     * @implNote optimised for very large arrays
     * @param array ({@code Object[]}): array to search
     * @param val ({@code Object}): value to find
     * @return (int): index of the value in the array
     */
    public static int quickFind(Object[] array, final Object val) {
        Objects.requireNonNull(array);

        // places element we are searching for at the end of the array
        // and saves the element previously there
        final Object temp = array[array.length - 1];
        array[array.length - 1] = val;

        // find the element we are searching for in the array
        int counter = -1;
        while (!Objects.equals(array[++counter], val));

        // restores the array to its previous state
        array[array.length - 1] = temp;
        
        // returns the position of the value in the array
        return Objects.equals(array[counter], val) ? counter : -1;
    }

    /**
     * Finds the first occurrence of given values in an array
     * @implNote optimised for very large arrays
     * @param array ({@code Object[]}): array to search
     * @param values ({@code Collection\u003C ? \u003E}): values to find
     * @return (int[]): index of the values in the array
     */
    public static int[] quickFind(Object[] array, final Collection<?> values) {
        return quickFind(array, values.toArray());
    }

    /**
     * Finds the first occurrence of given values in an array
     * @implNote optimised for very large arrays
     * @param array ({@code Object[]}): array to search
     * @param values ({@code Collection\u003C ? \u003E}): values to find
     * @return (int[]): index of the values in the array
     */
    public static int[] quickFind(Object[] array, final Object[] values) {
        Objects.requireNonNull(array);

        // checks that the values array is not null or empty
        if (values == null || values.length == 0) return new int[0];

        // pais each element in the array to its index in a HashMap
        final HashMap<Object, Integer> contained = new HashMap<>(array.length);
        for (int i = 0; i < array.length; i++) {
            contained.put(array[i], i);
        }

        // iterates over every value to remove...
        final int[] searchIndexes = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            // ...gets the index of each value and saves it
            // -1 index is substituted to null when value was not found in HashMap (ie: it is not is array)
            final Integer index = contained.get(values[i]);
            searchIndexes[i] = index == null ? -1 : index;
        }

        // returns the index of each value in the array
        return searchIndexes;
    }

    /**
     * Finds all occurrence of given values in an array
     * @implNote optimised for very large arrays
     * @param array ({@code Object[]}): array to search
     * @param toFind ({@code Collection\u003C ? \u003E}): values to find
     * @return (int[]): index of the values in the array
     */
    public static <T> int[] quickFindAll(
            @NotNull final T[] array,
            final Object[] toFind
    ) {
        Objects.requireNonNull(array);

        // checks that the values array is not null or empty
        if (toFind == null || toFind.length == 0) return new int[0];

        // used for comparing objects even if they do not implement the Comparable interface
        final ObjectComparator comparator = new ObjectComparator();

        // sorts out duplicate values to find
        final Object[] blindUniqueToFind = retainDistinctImpl(toFind, comparator);

        // builds the search array
        final Object[][] a = new Object[][]{{Integer.MIN_VALUE, -1}};
        final Object[][] b = wrapArrayIndexes(array);
        final Object[][] searchArray = concatenate(Object[].class, a, b);

        // sorts search array
        Arrays.parallelSort(searchArray, 1, searchArray.length, new ArrayElementComparator(0));

        // counter variables for the loop*
        int i, j, k;
        i = searchArray.length;
        j = blindUniqueToFind.length - 1;
        k = 0;

        // stores all found indexes
        final int[] blindResult = new int[array.length];

        // keeps looking for values to find while not all values have been checked for
        while (j >= 0) {
            // compares the current value to the previous element in the array
            final var val = searchArray[--i];
            final var search = blindUniqueToFind[j];
            // if the previous element in the array is superior or equal to the value we are checking for,
            // then it might be a valid match
            final boolean superiorOrEqual = comparator.compare(val[0], search) >= 0;
            if (superiorOrEqual) {
                // saves the index of the element if it is a match
                if (Objects.equals(val[0], search)) blindResult[k++] = (int) val[1];
            }
            // if the previous element is inferior to the value we are checking for then, because the arrays are sorted,
            // we know that there are no elements before that which can be equal to the value, so we move on to the
            // previous value to find
            else {
                j--;
                i++; // we also increment i so that we can check this index again with the new value
            }
        }

        // trims down result to an array which fits the number of saved indexes
        return Arrays.copyOf(blindResult, k);
    }

    /**
     * Wraps array values in a 2D array mapping the original value to its index
     * (useful when an algorithm requires a sorted array, but it is necessary to reverse the sorting to get results
     * relative to the original array)
     * @param array ({@code Object[]}): array to wrap
     * @return (Object[][]): 2D array mapping value to their index
     */
    private static Object[][] wrapArrayIndexes(
            final Object[] array
    ) {
        final Object[][] result = new Object[array.length][2];
        for (int i = 0; i < array.length; i++) {
            result[i] = new Object[]{array[i], i};
        }
        return result;
    }

    /**
     * Removes the specified values from the array
     * @param array ({@code E[]}): array from which to remove the values
     * @param clazz ({@code Class\u003C E \u003E}): array element's class
     * @param c ({@code Collection \u003C ? \u003E}): values to remove
     * @return (E[]) array of retained values
     * @param <E> class of the array
     */
    public static <E> E[] batchRemove(
            final E[] array,
            final Class<E> clazz,
            final Collection<?> c
    ) {
        return batchEdit(array, clazz, c, false);
    }

    /**
     * Keeps the specified values from the array
     * @param array ({@code E[]}): array from which to retain the values
     * @param clazz ({@code Class\u003C E \u003E}): array element's class
     * @param c ({@code Collection \u003C ? \u003E}): values to retain
     * @return (E[]) array of retained values
     * @param <E> class of the array
     */
    public static <E> E[] batchRetain(
            final E[] array,
            final Class<E> clazz,
            final Collection<?> c
    ) {
        return batchEdit(array, clazz, c, true);
    }

    /**
     * Either keeps only the values in the array which are contained in the collection
     * or only those which are not part of the collection, depending on the retain mode
     * @param array ({@code E[]}): base array
     * @param clazz ({@code Class\u003C E \u003E}): array element's class
     * @param c ({@code Collection \u003C ? \u003E}): values to retain / remove
     * @param retain ({@code boolean}): retain mode (false = remove elements, true = keep elements)
     * @return (E[]) array of resulting values
     * @param <E> class of the array
     */
    private static <E> E[] batchEdit(
            final E[] array,
            final Class<E> clazz,
            final Collection<?> c,
            final boolean retain
    ) {
        final E[] blindResult = (E[]) Array.newInstance(clazz, array.length);

        int i = 0, k = 0;
        while (++i < array.length) {
            if (c.contains(array[i]) == retain) blindResult[k++] = array[i];
        }

        return Arrays.copyOf(blindResult, k);
    }

    public static void main(String[] args) {
        final Integer[] array = new Integer[]{12, 1, 3, 5, 4, 3, 0, 12};
        final Integer[] toFind = new Integer[]{12, 0};

        final long start = System.currentTimeMillis();
        System.out.println(Arrays.toString(retainDistinct(array)));
        final long stop = System.currentTimeMillis();
        System.out.println(stop - start);
    }
}
