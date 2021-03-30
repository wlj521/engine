package com.wlj.engine.likou;

public class SolutionMaxPower {

    public int maxPower(String s) {
        int count = 1;
        int max = 1;
        if (s.length() == 1) {
            return count;
        }
        char first = s.charAt(0);
        for (int i = 1; i < s.length(); i++) {
            if (first == s.charAt(i)) {
                count++;
                max = Math.max(count, max);
            } else {
                first = s.charAt(i);
                count = 1;
            }
        }
        return max;
    }
}
