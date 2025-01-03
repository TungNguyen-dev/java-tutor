package com.tungnn.java.leetcode;

import java.util.Arrays;

public class RunningSum1DArray {

    public static void main(String[] args) {
        /*
         * Given an array nums. We define a running sum of an array as
         * runningSum[i] = sum(nums[0]…nums[i]).
         *
         * Return the running sum of nums.
         */
        int[] nums = new int[] {1,2,3,4};
        System.out.println(Arrays.toString(runningSum(nums)));
    }

    public static int[] runningSum(int[] nums) {
        int[] runningSum = new int[nums.length];

        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            runningSum[i] = sum;
        }

        return runningSum;
    }
}
