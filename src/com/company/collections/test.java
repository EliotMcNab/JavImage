package com.company.collections;

import java.util.ArrayList;
import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        /*final ImmutableList<Character> immutableList1 = new ImmutableList<>(new Character[]{'a', 'b', 'c', 'd', 'e'});
        final ImmutableList<Character> immutableList2 = immutableList1.add('f');
        System.out.println(immutableList1);
        System.out.println(Arrays.toString(immutableList2.toArray()));*/

        final long start = System.currentTimeMillis();

        final ImmutableList<Integer> immutableList3 = new ImmutableList<>();
        final ArrayList<Integer> arrayList = new ArrayList<>(2_073_600);

        for (int i = 0; i < 2_073_600; i++) {
            arrayList.add(i);
        }

        immutableList3.addAll(arrayList);
        System.out.println(System.currentTimeMillis() - start);
        // System.out.println(immutableList3);
    }
}
