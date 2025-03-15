package com.tungnn.java.leetcode;

import java.util.Arrays;

public class RemoveDuplicatesFromSortedArray {

    public static void main(String[] args) {
        int[] nums = new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        System.out.println(Arrays.toString(nums));

        int k = removeDuplicates(nums);
        System.out.println(Arrays.toString(nums));
        System.out.println(k);
    }

    public static int removeDuplicates(int[] nums) {
        int k = 0;

        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] == nums[j]) {
                    i++;
                } else {
                    break;
                }
            }

            nums[k] = nums[i];
            k++;
        }

        return k;
    }
}
