package com.company.collections;

import com.company.collections.changes.Change;

import java.util.*;

public class test {
    public static void main(String[] args) {
        // region removal tests

        final int MAX_VALUE = 1_000_000;
        final Random random = new Random();

        final Integer[] array = new Integer[MAX_VALUE];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(MAX_VALUE);
        }
        
        final Integer[] toRemove = new Integer[1_000];
        for (int i = 0; i < toRemove.length; i++) {
            toRemove[i] = random.nextInt(MAX_VALUE);
        }

        final ArrayList<Integer> arrayList1 = new ArrayList<>(array.length);

        final long changeStart = System.currentTimeMillis();
        final Integer[] removed = Change.of(Integer.class).addAll(array).removeAll(toRemove).removeFirst(2).toArray();
        final long changeStop = System.currentTimeMillis();

        final long arrayStart = System.currentTimeMillis();
        arrayList1.addAll(Arrays.asList(array));
        arrayList1.removeAll(List.of(toRemove));
        arrayList1.remove(Integer.valueOf(2));
        final long arrayStop = System.currentTimeMillis();

        boolean sizeError = removed.length != arrayList1.size();

        boolean removalError = sizeError;
        if (!sizeError) {
            for (int i = 0; i < removed.length; i++) {
                if (!Objects.equals(arrayList1.get(i), removed[i])) removalError = true;
            }
        }

        System.out.println(">> SIZE DISCREPANCIES");
        System.out.println(sizeError ? "YES" : "NONE");
        System.out.println("Change: " + removed.length);
        System.out.println("ArrayList  : " + arrayList1.size());
        System.out.println(">> REMOVAL DISCREPANCIES");
        System.out.println(removalError ? "YES" : "NONE");
        System.out.println(">> ALGORITHM DURATION");
        System.out.println("Change: " + (changeStop - changeStart) + "ms");
        System.out.println("ArrayList  : " + (arrayStop - arrayStart) + "ms");

        // endregion

        // region retaining tests

        /*final Integer[] array = new Integer[1_000_000];
        final Integer[] toRetain = new Integer[500_000];
        final Random random = new Random();

        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(10);
        }

        for (int i = 0; i < toRetain.length; i++) {
            toRetain[i] = random.nextInt(10);
        }

        final RetainFirst<Integer> retainChange = new RetainFirst<>(toRetain);
        final long retainStart = System.currentTimeMillis();
        final Integer[] retained1 = retainChange.applyTo(array, Integer.class);
        final long retainStop = System.currentTimeMillis();

        final ArrayList<Integer> retained2 = new ArrayList<>(Arrays.asList(array));
        final ArrayList<Integer> toRetainList = new ArrayList<>(Arrays.asList(toRetain));
        final long listStart = System.currentTimeMillis();
        // retained2.retainAll(toRetainList);
        final long listStop = System.currentTimeMillis();

        //final char[] retainVisual = new char[array.length];
        //
        //Arrays.fill(retainVisual, '-');
        //final int[] retainedLocations = Arrays.stream(ArrayUtil.quickFindAll(array, toRetain))
        //                                      .filter(value -> value >= 0)
        //                                      .toArray();
        //for (Integer i : retainedLocations) {
        //    retainVisual[i] = 'x';
        //}
        //
        //System.out.println(">> ARRAYS");
        //System.out.println("to retain: " + Arrays.toString(toRetain));
        //System.out.println(Arrays.toString(array));
        //System.out.println(Arrays.toString(retainVisual));
        //System.out.println(">> RETAINED");
        //System.out.println("retained: " + Arrays.toString(retained));
        System.out.println(">> ALGORITHM DURATION");
        System.out.println("RetainFirst: " + (retainStop - retainStart) + "ms");
        System.out.println("ArrayList : " + (listStop - listStart) + "ms");*/

        // endregion
    }
}
