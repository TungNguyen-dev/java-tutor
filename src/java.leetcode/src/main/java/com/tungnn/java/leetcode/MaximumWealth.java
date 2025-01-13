package com.tungnn.java.leetcode;

public class MaximumWealth {

    public static void main(String[] args) {
        /*
         * You are given an m x n integer grid accounts where accounts[i][j]
         * is the amount of money the i customer has in the j bank.
         * Return the wealth that the richest customer has.
         * A customer's wealth is the amount of money they have
         * in all their bank accounts. The richest customer is the customer
         * that has the maximum wealth.
         */
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        System.out.println(maximumWealth(matrix));
        System.out.println(maximumWealth2(matrix));
    }

    public static int maximumWealth(int[][] accounts) {
        int[] accountAmounts = new int[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            for (int j = 0; j < accounts[i].length; j++) {
                accountAmounts[i] += accounts[i][j];
            }
        }

        int maximumWealth = 0;
        for (int accountAmount : accountAmounts) {
            maximumWealth = Math.max(maximumWealth, accountAmount);
        }

        return maximumWealth;
    }

    public static int maximumWealth2(int[][] accounts) {
        int maxWealth = 0;
        for (int[] person : accounts) {
            int tmpWealth = 0;
            for (int account : person) {
                tmpWealth += account;
            }

            maxWealth = Math.max(maxWealth, tmpWealth);
        }

        return maxWealth;
    }
}
