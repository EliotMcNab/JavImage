package com.company.collections;

import java.util.ArrayList;
import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        /*final ListChange<Integer> listChange1 = new ListChange<>();
        listChange1.add(0);
        System.out.println(listChange1);
        System.out.println(listChange1.size());
        listChange1.addAll(Arrays.asList(1, 2, 3, 4, 5, 6));
        for (Integer integer : listChange1) {
            System.out.println(integer);
        }
        System.out.println(listChange1);
        System.out.println(listChange1.size());
        listChange1.remove(6);
        System.out.println(listChange1);
        System.out.println(listChange1.size());
        listChange1.removeAll(Arrays.asList(1, 5));
        System.out.println(listChange1);
        System.out.println(listChange1.size());
        listChange1.retainAll(Arrays.asList(2, 4));
        System.out.println(listChange1);
        System.out.println(listChange1.size());
        listChange1.clear();
        System.out.println(listChange1);
        System.out.println(listChange1.size());*/

        final ListChange<Integer> listChange = new ListChange<>();
        ArrayList<Integer> arrayList = new ArrayList<>(2_073_600);
        for (int i = 0; i < 2_073_600; i++) {
            arrayList.add(i);
        }
        listChange.addAll(arrayList);

        System.out.println(System.currentTimeMillis() - start);
    }
}
