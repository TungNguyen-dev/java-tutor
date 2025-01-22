package com.tungnn.java.leetcode;

public class RomanToInteger {

    /**
     *
     * @param s an Roman {@code String}
     * @return integer of Roman string
     */
    public static int romanToInt(String s) {
        int result = 0;

        for (int i = 0; i < s.length(); i++) {
            char currChar = s.charAt(i);
            char nextChar = i < s.length() - 1 ? s.charAt(i + 1) : '\u0000';

            int cVal = switch (currChar) {
                case 'I' -> {
                    if (nextChar == 'V') {
                        i++;
                        yield 4;
                    } else if (nextChar == 'X') {
                        i++;
                        yield 9;
                    } else {
                        yield 1;
                    }
                }
                case 'V' -> 5;
                case 'X' -> {
                    if (nextChar == 'L') {
                        i++;
                        yield 40;
                    } else if (nextChar == 'C') {
                        i++;
                        yield 90;
                    } else {
                        yield 10;
                    }
                }
                case 'L' -> 50;
                case 'C' -> {
                    if (nextChar == 'D') {
                        i++;
                        yield 400;
                    } else if (nextChar == 'M') {
                        i++;
                        yield 900;
                    } else {
                        yield 100;
                    }
                }
                case 'D' -> 500;
                case 'M' -> 1000;
                default -> 0;
            };

            result += cVal;
        }

        return result;
    }
}
