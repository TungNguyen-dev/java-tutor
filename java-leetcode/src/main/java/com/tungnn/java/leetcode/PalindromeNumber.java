package com.tungnn.java.leetcode;

public class PalindromeNumber {

    /**
     * Check if an integer is a palindrome when it reads the same forward and
     * backward.
     * <p>
     * For example, 121 is a palindrome while 123 is not.
     *
     * @param x an {@code int}
     * @return {@code boolean}
     */
    public boolean isPalindrome(int x) {
        String str = String.valueOf(x);
        String reversed = new StringBuilder(str).reverse().toString();

        return str.equals(reversed);
    }
}
