package com.company.collections;

import com.company.collections.changes.ArrayRemove;
import com.company.utilities.ArrayUtil;

import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class test {
    public static void main(String[] args) {
        final Integer[] testArray = new Integer[2_000_000];
        for (int i = 0; i < 2_000_000; i++) {
            testArray[i] = i;
        }
        
        final ArrayList<Integer> toRemove = new ArrayList<>(1_000);
        final Random random = new Random();
        for (int i = 0; i < 1_000; i++) {
            toRemove.add(random.nextInt(2_000_000));
        }
        final ArrayRemove<Integer> arrayRemove1 = new ArrayRemove<>(toRemove);

        /*final long start = System.currentTimeMillis();
        arrayRemove1.applyTo(testArray);
        System.out.println(System.currentTimeMillis() - start);*/

        final ArrayList<Integer> arrayList1 = new ArrayList<>(2_000_000);
        arrayList1.addAll(Arrays.asList(testArray));

        final long start = System.currentTimeMillis();
        arrayList1.removeAll(toRemove);
        System.out.println(System.currentTimeMillis() - start);
    }
}
