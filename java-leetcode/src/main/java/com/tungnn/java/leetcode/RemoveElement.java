package com.tungnn.java.leetcode;

import java.util.Arrays;

public class RemoveElement {

    public static void main(String[] args) {
        int[] nums = new int[]{3, 2, 2, 3};
        System.out.println(removeElement(nums, 3));
        System.out.println(Arrays.toString(nums));
    }

    public static int removeElement(int[] nums, int val) {
        int k = 0;

        if (nums == null || nums.length == 0) {
            return 0;
        }

        main:
        for (int i = 0; i < nums.length; i++) {
            for (int j = i; j < nums.length; j++) {
                if (nums[i] == val) {
                    if (j < nums.length - 1) {
                        i++;
                    } else {
                        break main;
                    }
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
