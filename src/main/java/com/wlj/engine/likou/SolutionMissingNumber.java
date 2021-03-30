package com.wlj.engine.likou;

public class SolutionMissingNumber {

    public static int missingNumber(int[] nums) {
        int i = 0, j = nums.length - 1;
        while (i <= j) {
            int m = (i + j) / 2;
            if (nums[m] == m) {
                i = m + 1;
            } else {
                j = m - 1;
            }
        }
        return i;
    }

    public static void main(String[] args) {
        int[] a = {1, 3, 4};
        int b = missingNumber(a);
        System.out.println(b);

        String url = "https://cdn.poizon.com/node-common/1616302788049.1814?attname=play.65c7204e.svg";
        String[] urls = url.split("\\.");
        System.out.println(urls[urls.length-1]);
    }
}
