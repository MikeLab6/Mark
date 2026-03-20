package com.example;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author: 杨洪飞
 * @date: 2024/09/13 09:41:46
 */
@Slf4j
public class Tool {

    public static int[] sort(int[] list) {

        for (int i = 0, l = list.length; i < l; i++) {
            for (int j = i + 1; j < list.length; j++) {
                if (list[i] < list[j]) {
                    int tmp = list[i];
                    list[i] = list[j];
                    list[j] = tmp;
                }
            }
        }
        return list;
    }


    public static void main(String[] args) {
        // int[] test= {1,3,5,7,9};
        //int[] result= sort(test);
        // for (int i = 0, l = result.length; i < l; i++) {
        //     System.out.println(result[i]+",");
        // }
        //Map<Integer, String> mmm = Maps.newHashMapWithExpectedSize(1000000);
        //Map<Integer, String> mmm = new HashMap<>();
        //long start = nanoTime();
        //for (int i = 0, l = 1000000; i < l; i++) {
        //    mmm.put(RandomUtilrandomInt(), String.valueOf(i));
        //
        //}
        //Vector
        //Collections.synchronizedList()
        //mmm.putAll();
        //ThreadLocal t=new ThreadLocal();
        //t.set();
        //List<String> ss=new LinkedList<>();
        //ss.get()
        //Collections.synchronizedMap()
        //log.warn("yhf-------" + (nanoTime() - start)/1000000);

        List<Integer> test = new ArrayList<>();
        test.add(7);
        test.add(44);
        test.add(22);
        test.add(98);
        test.add(11);
        test.add(33);
        test.add(27);
        quickSort(test, 0, test.size() - 1);
        for (Integer integer : test) {
            System.out.println(integer);
        }

    }

    private static void quickSort(List<Integer> test, int left, int right) {
        if (left < right) {
            int idx = findPart(test, left, right);
            quickSort(test, 0, idx - 1);
            quickSort(test, idx + 1, right);

        }


    }

    private static int findPart(List<Integer> test, int left, int right) {
        int mark = test.get(right);
        int j = left;
        int k = right - 1;
        while (j < k) {


            while (test.get(j) < mark) {
                j++;

            }
            while (test.get(k) > mark) {
                k--;

            }
            if (j < k) {
                swap(test, j, k);

            }

        }
        //for (int j = left; j < right; j++) {
        //
        //    if (test.get(j) < mark) {
        //        swap(test, i++, j);
        //    }
        //}
        if (j < right && test.get(j) > test.get(right)) {
            swap(test, j, right);

        }

        return j;
    }

    private static void swap(List<Integer> test, int i, int j) {
        int tmp = test.get(i);
        test.set(i, test.get(j));
        test.set(j, tmp);
    }


}
