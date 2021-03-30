package com.wlj.engine;

import java.util.Arrays;

public class SortTest {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(bubbling()));
        System.out.println(Arrays.toString(select()));
    }

    /**
     * 冒泡排序
     *
     * @return
     */
    private static int[] bubbling() {
        int[] arr = {2, 7, 8, 4, 9, 3, 21, 6};
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
        return arr;
    }

    /**
     * 选择排序
     *
     * @return
     */
    private static int[] select() {
        int[] arr = {2, 7, 8, 4, 9, 3, 21, 6};
        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }
            if (i != min) {
                int tmp = arr[i];
                arr[i] = arr[min];
                arr[min] = tmp;
            }
        }
        return arr;
    }
}
