package com.tungnn.java.leetcode;

import java.util.HashMap;
import java.util.Map;

public class RansomNote {

    public static boolean canConstruct(String ransomNote, String magazine) {
        if (ransomNote == null || magazine == null) {
            return false;
        }

        for (char c : ransomNote.toCharArray()) {
            int index = magazine.indexOf(c);

            if (index == -1) {
                return false;
            }

            magazine = magazine.substring(0, index) +
                       magazine.substring(index + 1);
        }

        return true;
    }

    public static boolean canConstruct2(String ransomNote, String magazine) {
        Map<Character, Integer> magazineLetterMap = new HashMap<>();

        for (int i = 0; i < magazine.length(); i++) {
            char m = magazine.charAt(i);

            int currCount = magazineLetterMap.getOrDefault(m, 0);
            magazineLetterMap.put(m, currCount + 1);
        }

        for (int i = 0; i < ransomNote.length(); i++) {
            char r = ransomNote.charAt(i);

            int currCount = magazineLetterMap.getOrDefault(r, 0);
            if (currCount == 0) {
                return false;
            }

            magazineLetterMap.put(r, currCount - 1);
        }

        return true;
    }
}
