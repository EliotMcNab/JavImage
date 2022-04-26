package com.company.collections;

import com.company.collections.changes.ArrayAdd;
import com.company.collections.changes.ArrayRemove;
import com.company.collections.changes.ArrayRetain;
import com.company.utilities.ArrayUtil;

import java.lang.module.FindException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class test {
    public static void main(String[] args) {
        final Integer[] testArray = new Integer[1_000_000];
        for (int i = 0; i < 1_000_000; i++) {
            testArray[i] = i;
        }
        
        final ArrayList<Integer> values = new ArrayList<>(10_000);
        final Random random = new Random();
        for (int i = 0; i < 10_000; i++) {
            values.add(random.nextInt(1_000_000));
        }

        final ArrayRemove<Integer> arrayRemove1 = new ArrayRemove<>(values);
        final ArrayList<Integer> arrayList1 = new ArrayList<>(testArray.length);
        arrayList1.addAll(Arrays.asList(testArray));

        final long changeStart = System.currentTimeMillis();
        final Integer[] removed = arrayRemove1.applyTo(testArray, Integer.class);
        final long changeStop = System.currentTimeMillis();

        final long arrayStart = System.currentTimeMillis();
        arrayList1.removeAll(values);
        final long arrayStop = System.currentTimeMillis();

        boolean error = false;
        for (int i = 0; i < removed.length; i++) {
            if (!Objects.equals(arrayList1.get(i), removed[i])) error = true;
        }

        System.out.println(">> REMOVAL DISCREPANCIES");
        System.out.println(error ? "YES" : "NONE");
        System.out.println(">> ALGORITHM DURATION");
        System.out.println("ArrayChange: " + (changeStop - changeStart) + "ms");
        System.out.println("ArrayList: " + (arrayStop - arrayStart) + "ms");
    }
}
