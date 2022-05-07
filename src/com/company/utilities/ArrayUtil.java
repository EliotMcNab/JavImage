package com.company.utilities;

import com.company.collections.changeAPI.Change;
import com.company.utilities.comparators.ArrayElementComparator;
import com.company.utilities.comparators.ObjectComparator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;

public class ArrayUtil {

    public static int determineTotalLength(final Object[][] array) {
        // TODO: make this work for n-dimensional arrays
        int length = 0;

        for (Object[] objects : array) {
            length += objects.length;
        }

        return length;
    }

    /**
     * Finds the indexes of all values matching the given predicate
     * @param array ({@code T[]}): array of values to check against predicate
     * @param filter ({@code Predicate<? super T>}): predicate to check against values
     * @return (boolean): index of all values matching the predicate
     * @param <T> type of the array
     */
    public static <T> int[] findMatches(
            @NotNull final T[] array,
            @NotNull final Predicate<? super T> filter
    ) {
        final int[] result = new int[array.length];

        int k = 0;
        for (int i = 0; i < array.length; i++) {
            if (filter.test(array[i])) result[k++] = i;
        }
        return Arrays.copyOf(result, k);
    }

    public static <T> T[] retainMatches(
            @NotNull final T[] array,
            @NotNull final Predicate<? super T> filter
    ) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(filter);
        if (array.length == 0) return array;

        final T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);

        int k = 0;
        for (T t : array) {
            if (filter.test(t)) result[k++] = t;
        }

        return Arrays.copyOf(result, k);
    }

    public static <T> T[] retainAtMultipleIndexes(
            @NotNull final T[] array,
            final int num,
            final boolean invert
    ) {
        Objects.requireNonNull(array);
        if (array.length == 0) return array;

        final T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length / num);

        int k = 0;
        for (int i = 0; i < array.length; i++) {
            if ((i % num == 0) != invert) result[k++] = array[i];
        }

        return Arrays.copyOf(result, k);
    }

    @Contract(mutates = "param1")
    public static void swap(Object[] array, final int a, final int b) {
        final Object temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    /**
     * Keeps only the distinct elements in an array
     * @param array ({@code int[]}): array to check the values of (<strong>MUST BE SORTED</strong>)
     * @return (int[]): distinct elements in the array
     */
    public static int[] retainDistinct(
            final int @NotNull [] array
    ) {
        // checks the validity of parameter
        Objects.requireNonNull(array);;
        if (array.length == 0) return array;

        final int[] blindResult = new int[array.length];

        blindResult[0] = array[0];

        int i, k;
        for (i = 1, k = 1; i < array.length; i++) {
            if (array[i] != array[i-1]) blindResult[k++] = array[i];
        }

        return Arrays.copyOf(blindResult, k);
    }

    /**
     * Keeps only the distinct elements in an array
     * @param array ({@code T[]}): array to check the values of (<strong>MUST BE SORTED</strong>)
     * @return (T[]): distinct elements in the array
     * @param <T> class of the array
     */
    public static <T> T[] retainDistinct(
            @NotNull final T[] array
    ) {
        return retainDistinct(array, (Comparator<T>) new ObjectComparator());
    }

    public static <T> T[] retainDistinct(
            @NotNull final T[] array,
            @NotNull final Comparator<T> comparator
    ) {
        Objects.requireNonNull(comparator);
        Objects.requireNonNull(array);

        final Object[][] wrapped = wrapArrayIndexes(array);
        final Object[] distinct = retainDistinctImpl(wrapped, new ArrayElementComparator<>(0, (Comparator<Object>) comparator));
        final T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), distinct.length);

        Arrays.parallelSort(distinct, new ArrayElementComparator(1));

        int k = 0;
        for (Object o : distinct) {
            result[k++] = (T) ((Object[]) o)[0];
        }

        return result;
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
        // checks the validity of parameter
        Objects.requireNonNull(array);
        Objects.requireNonNull(comparator);
        if (array.length == 0) return array;

        final T[] arrayCopy = Arrays.copyOf(array, array.length);
        final Object[] blindResult = new Object[array.length];

        Arrays.parallelSort(arrayCopy, comparator);
        blindResult[0] = arrayCopy[0];

        int i, k;
        for (i = 1, k = 1; i < arrayCopy.length; i++) {
            if (comparator.compare(arrayCopy[i], arrayCopy[i-1]) != 0) blindResult[k++] = arrayCopy[i];
        }

        return (T[]) Arrays.copyOf(blindResult, k);
    }

    /**
     * Creates a new array with only the elements at the specified indexes
     * @param array ({@code T[]}): array containing the values to retain
     * @param indexes ({@code int[]}): indexes of the values to retain
     * @return array containing only the values to retain
     * @param <T> type of the array
     */
    public static <T> T[] retainAt(
            final T @NotNull [] array,
            final int @NotNull [] indexes
    ) {
        // checks the validity of the arguments
        Objects.requireNonNull(array);
        Objects.requireNonNull(indexes);
        if (array.length == 0) return array;

        // creates the blind result array
        final T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);

        // iterates over the indexes to keep
        int k = 0;
        for (int i : indexes) {
            // if the index is valid, adds it to the result array
            if (i >= 0 && i < array.length) result[k++] = array[i];
        }

        // returns the final result
        return Arrays.copyOf(result, k);
    }

    /**
     * Creates a new array without the elements at the specified indexes
     * @param array ({@code T[]}): array containing the values to remove (needs to be sorted)
     * @param indexes ({@code int[]}): indexes of the values to remove
     * @return array containing without the elements to remove
     * @param <T> type of the array
     */
    public static <T> T[] removeAt(
            final T @NotNull [] array,
            final int @NotNull [] indexes
    ) {
        // checks the validity of the arguments
        Objects.requireNonNull(array);
        Objects.requireNonNull(indexes);
        if (array.length == 0) return array;

        // creates the blind result array
        final T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);

        // iterates over the indexes to remove
        int lastIndex = 0, k = 0;
        for (int i : indexes) {
            // if the index is negative (ie: corresponds to an element which wasn't found)
            // or greater than the size of the array, moves on
            if (i < 0 || i >= array.length) continue;

            // copies the elements between the previous index to remove and the current index to remove
            final int length = i - k;
            System.arraycopy(array, k, result, lastIndex, length);
            k += length + 1;
            lastIndex += length;
        }

        // copies over any remaining elements after the last index to remove
        System.arraycopy(array, k, result, lastIndex, array.length - k);
        lastIndex += array.length - k;

        // returns the resulting array
        return Arrays.copyOf(result, lastIndex);

    }

    /**
     * Concatenates multiple arrays together
     * @param arrays ({@code T[]...}): arrays to concatenate
     * @return resulting array concatenation
     * @param <T> class of the array
     */
    @SafeVarargs
    public static <T> T[] concatenate(
            @NotNull final T[]... arrays
    ) {
        int totalLength = 0;
        for (T[] array : arrays) {
            totalLength += array.length;
        }

        final Class<?> clazz = arrays.getClass().componentType().componentType();
        final T[] result = (T[]) Array.newInstance(clazz, totalLength);

        int k = 0;
        for (T[] array : arrays) {
            System.arraycopy(array, 0, result, k, array.length);
            k += array.length;
        }

        return result;
    }

    public static int[] concatenate(
            final int[]... arrays
    ) {
        int totalLength = 0;
        for (int[] array : arrays) {
            totalLength += array.length;
        }

        final int[] result = new int[totalLength];

        int k = 0;
        for (int[] array : arrays) {
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

    public static int[] quickFindFirst(
            @NotNull final Object[] array,
            final Object toFind
    ) {
        return quickFindFirst(array, new Object[]{toFind});
    }

    public static int[] quickFindFirst(
            @NotNull final Object[] array,
            final Object[] toFind
    ) {
        // checks the validity of the arguments
        Objects.requireNonNull(array);
        if (toFind == null || toFind.length == 0) return new int[0];

        // keeps only unique values to find and maps them to their current index
        // (this is so that even after sorting their initial index can be retrieved)
        final Object[][] uniqueToFind = wrapArrayIndexes(retainDistinct(toFind));

        // creates the search array
        final Object[][] a = new Object[][]{{Integer.MIN_VALUE, -1}};
        final Object[][] b = wrapArrayIndexes(array);
        final Object[][] search = concatenate(a, b);

        // creates the blind result array
        final int[] result = new int[uniqueToFind.length];
        Arrays.fill(result, Integer.MAX_VALUE);

        // creating comparators
        final Comparator<Object[]> zerothComparator = new ArrayElementComparator<>(0); // used for sorting arrays
        final Comparator<Object> comparator = new ObjectComparator();              // used for comparing array elements

        // sorting arrays
        Arrays.parallelSort(uniqueToFind, zerothComparator);
        Arrays.parallelSort(search, 1, search.length, zerothComparator);

        // Initialising indexes
        int i = search.length;
        int j = uniqueToFind.length - 1;

        // search loop
        while (j >= 0) {
            i--; // moves on to the next element in the search array

            final int comparison = comparator.compare(search[i][0], uniqueToFind[j][0]);
            final int index = (int) uniqueToFind[j][1];

            // if search[i][0] < uniqueToFind[j]
            if (comparison < 0) {
                // sets the previous value's index to -1 if no match has been found
                if (result[index] == Integer.MAX_VALUE) result[index] = -1;

                j--; // moves on to the next element to search
                i++; // compares it against the current element

                continue;
            }

            // if search[i][0] == uniqueToFind[j] && search[i][1] < result[index]
            if (comparison == 0 && comparator.compare(search[i][1], result[index]) < 0) {
                result[index] = (int) search[i][1];
            }
        }

        // returns the final array of indexes
        return result;
    }

    public static <T> int[] quickFindAll(
            @NotNull final T[] array,
            final Object toFind
    ) {
        return quickFindAll(array, new Object[]{toFind});
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
        final Object[] uniqueToFind = retainDistinctImpl(toFind, comparator);

        // builds the search array
        final Object[][] a = new Object[][]{{Integer.MIN_VALUE, -1}};
        final Object[][] b = wrapArrayIndexes(array);
        final Object[][] searchArray = concatenate(a, b);

        // sorts search array
        Arrays.parallelSort(searchArray, 1, searchArray.length, new ArrayElementComparator<>(0));

        // counter variables for the loop*
        int i, j, k;
        i = searchArray.length;
        j = uniqueToFind.length - 1;
        k = 0;

        // stores all found indexes
        final int[] blindResult = new int[array.length];

        // keeps looking for values to find while not all values have been checked for
        while (j >= 0) {
            // compares the current value to the previous element in the array
            final var val = searchArray[--i];
            final var search = uniqueToFind[j];
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
    public static Object[][] wrapArrayIndexes(
            final Object[] array
    ) {
        final Object[][] result = new Object[array.length][2];
        for (int i = 0; i < array.length; i++) {
            result[i] = new Object[]{array[i], i};
        }
        return result;
    }

    public static <T> T[][] wrapArrays(
            @NotNull final T[]... arrays
    ) {
        Objects.requireNonNull(arrays);
        final Class<?> arrayClass = arrays.getClass().getComponentType().getComponentType();
        if (arrays.length == 0) return (T[][]) Array.newInstance(arrayClass, 0, 0);

        for (int i = 0; i < arrays.length - 1; i++) {
            if (arrays[i].length != arrays[i+1].length) throw new IllegalArgumentException(
                    "Arrays must be of equal size to be wrapped"
            );
        }

        // final Object[][] result = new Object[arrays[0].length][arrays.length];
        final T[][] result = (T[][]) Array.newInstance(arrayClass, arrays[0].length, arrays.length);

        for (int i = 0; i < arrays[0].length; i++) {
            final T[] element = (T[]) Array.newInstance(arrayClass, arrays.length);
            for (int j = 0; j < arrays.length; j++) {
                element[j] = arrays[j][i];
            }
            result[i] = element;
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

        int i = -1, k = 0;
        while (i < array.length - 1 && c.contains(array[k = ++i]) == retain) {
            if (i == array.length) return blindResult;
        }

        System.arraycopy(array, 0, blindResult, 0, k);

        while (++i < array.length) {
            if (c.contains(array[i]) == retain) blindResult[k++] = array[i];
        }

        return Arrays.copyOf(blindResult, k);
    }

    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        final Change<Integer> change = Change.of(Integer.class)
                                             .addAll(1, 5, 17, 19, 12, 6, 6, 5, 7, 8, 17)
                                             .optimise();
        System.out.println(System.currentTimeMillis() - start);

        final ArrayList<Integer> test = new ArrayList<>();

        System.out.println(change.sumOf());
    }
}
