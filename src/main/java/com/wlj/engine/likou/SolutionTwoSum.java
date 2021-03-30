package com.wlj.engine.likou;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SolutionTwoSum {

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>(16);
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{map.get(target - nums[i]), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{0};
    }

    public static void main(String[] args) {
        int[] a = {1, 3, 5, 7, 4, 8};
        int[] b = twoSum(a, 10);
        System.out.println(Arrays.toString(b));
    }
}
