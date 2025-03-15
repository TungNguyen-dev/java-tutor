package com.tungnn.java.leetcode;

public class LongestCommonPrefix {

    public static void main(String[] args) {

    }

    public static String longestCommonPrefix(String[] strs) {
        int minimumStrLength = strs[0].length();
        for (int i = 1; i < strs.length; i++) {
            minimumStrLength = Math.min(minimumStrLength, strs[i].length());
        }

        String prefix;
        for (int i = minimumStrLength; i > 0; i--) {
            prefix = strs[0].substring(0, i);

            boolean found = true;
            for (String s : strs) {
                if (!s.startsWith(prefix)) {
                    found = false;
                    break;
                }
            }

            if (found) {
                return prefix;
            }
        }

        return "";
    }
}
