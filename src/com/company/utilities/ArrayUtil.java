package com.company.utilities;

import java.util.Arrays;
import java.util.Collection;

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

        // TODO: find some optimisations for this where we don't have to loop around for each value

        // place all the values we are searching for at the start of the array
        final Object[] searchArray = new Object[array.length + values.length];
        System.arraycopy(values, 0, searchArray, 0, values.length);
        System.arraycopy(array, 0, searchArray, values.length, array.length);

        // iterates through every value...
        final int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            // ...finds that value's position in the array
            int counter = searchArray.length;
            while (searchArray[--counter] != values[i]);
            result[i] = counter - values.length;
        }

        // returns each value's position in the array
        return result;
    }
}
