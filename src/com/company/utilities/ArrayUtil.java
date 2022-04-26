package com.company.utilities;

import java.lang.reflect.Array;
import java.util.*;

public class ArrayUtil {
    public static int quickFind(Object[] array, final Object val) {
        // places element we are searching for at the end of the array
        // and saves the element previously there
        final Object temp = array[array.length - 1];
        array[array.length - 1] = val;

        // find the element we are searching for in the array
        int counter = -1;
        while (array[++counter] != val);

        // restores the array to its previous state
        array[array.length - 1] = temp;

        // returns the position of the value in the array
        return array[counter] == val ? counter : -1;
    }

    public static int[] quickFind(Object[] array, final Collection<?> values) {
        return quickFind(array, values.toArray());
    }

    public static int[] quickFind(Object[] array, final Object[] values) {
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

    public static int[] quickFindAll(final Object[] array, final Object[] values) {
        // stores the index of every element in the array
        // elements which occur multiple times have ALL their indexes stored inside a List
        final HashMap<Object, ArrayList<Integer>> container = new HashMap<>(array.length);
        for (int i = 0; i < array.length; i++) {
            final Object val = array[i];
            storeIndex(val, i, container);
        }

        // keeps only the distinct values among those to be found
        final Object[] uniqueValues = Arrays.stream(values).distinct().toArray();
        // array containing the index of each value. Since we do not know the number of occurrences of a given value
        // this has the size of the original array to handle with worst-case scenario where all elements of the original
        // array are contained in the values to be found (blind because it does not know the final size)
        final Integer[] blindResult = new Integer[array.length];

        // for every unique value to find...
        int lastIndex = 0;
        for (Object val : uniqueValues) {
            // ...gets that value's index in the HashMap
            final List<Integer> index = container.get(val);
            // substitutes null index for -1 (value is not in array)
            if (index == null) blindResult[lastIndex++] = -1;
            // if value is contained in array...
            else {
                // ...gets all its indexes in the array...
                final Integer[] indexArray = new Integer[index.size()];
                index.toArray(indexArray);
                // ...and copies them over to the result array
                System.arraycopy(indexArray, 0, blindResult, lastIndex, indexArray.length);
                // increments the last position at which indexes were added
                lastIndex += indexArray.length;
            }
        }

        // filters out null values from the blind result. This corresponds to the case where not all values in the
        // original array are contained in the values to be found, in which case the blind result array is too large
        // compared to the number of indexes it contains
        return Arrays.stream(blindResult).filter(Objects::nonNull).mapToInt(Integer::intValue).toArray();
    }

    /**
     * Used in quickFindAll to store all value occurrence indexes in a value -> {@code List<Integer>} Hashmap pair
     * @param value ({@code Object}): the value of which to save the index
     * @param index ({@code int}): index of the value
     * @param container ({@code HashMap<Object, T>}): HashMap containing value -> {@code List<Integer>} pairs
     * @param <T> ({@code <T extends List<Integer>})
     */
    private static <T extends List<Integer>> void storeIndex(
            final Object value,
            final int index,
            final HashMap<Object, T> container
    ) {
        // if the current value has not yet been stored...
        if (container.get(value) == null) {
            // ...creates a new ArrayList to store the value's index...
            final T initialList = (T) new ArrayList<Integer>();
            initialList.add(index);
            // ...and saves it
            container.put(value, initialList);
        }
        // if the current value was previously saved...
        else {
            // ...gets its previous indexes...
            final List<Integer> indexList = container.get(value);
            // ...and updates them
            indexList.add(index);
        }
    }

    public static void main(String[] args) {
        final Integer[] testArray = new Integer[1_000_000];
        for (int i = 0; i < testArray.length; i++) {
            testArray[i] = i;
        }

        final Random random = new Random();
        final Integer[] toRemove = new Integer[500_000];
        for (int i = 0; i < toRemove.length; i++) {
            toRemove[i] = random.nextInt(1_000_000);
        }

/*
        final Integer[] testArray = new Integer[]{1, 4, 4, 4, 5, 1, 7, 9, 0, 9, 1};
        final Integer[] toRemove = new Integer[]{1, 4, 9};
*/


        final long start = System.currentTimeMillis();
        final int[] potentialIndexes = quickFindAll(testArray, toRemove);
        final long stop = System.currentTimeMillis();
        final int[] indexes = Arrays.stream(potentialIndexes).filter(value -> value >= 0).toArray();


        final char[] displayRemove = new char[testArray.length];
        Arrays.fill(displayRemove, '-');
        for (int i : indexes) {
            displayRemove[i] = 'x';
        }

        System.out.println(">> NUMBER OF ELEMENTS");
        System.out.println("number of elements to remove: " + toRemove.length);
        System.out.println("number of unique elements to remove: " + Arrays.stream(toRemove).distinct().toArray().length);
        System.out.println("before removal: " + testArray.length);
        System.out.println("after removal: " + (testArray.length - indexes.length));
        /*System.out.println(">> REMOVAL LOCATIONS");
        System.out.println(Arrays.toString(testArray));
        System.out.println(Arrays.toString(displayRemove));*/
        System.out.println(">> ALGORITHM DURATION");
        System.out.println(stop - start + "ms");
    }
}
