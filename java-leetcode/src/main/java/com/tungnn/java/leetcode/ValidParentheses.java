package com.tungnn.java.leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

public class ValidParentheses {

    public boolean isValid(String s) {
        boolean result = true;

        char[][] parenthesesChars = {
            {'(', '{', '['},
            {')', '}', ']'}
        };
        Deque<Character> stack = new ArrayDeque<>();

        main:
        for (char c : s.toCharArray()) {
            for (int i = 0; i < parenthesesChars[0].length; i++) {
                char openChar = parenthesesChars[0][i];
                if (c == openChar) {
                    stack.push(c);
                    continue main;
                }
            }

            for (int i = 0; i < parenthesesChars[1].length; i++) {
                char openChar = parenthesesChars[0][i];
                char closeChar = parenthesesChars[1][i];
                if (c == closeChar) {
                    if (stack.isEmpty()) {
                        return false;
                    } else {
                        if (stack.peek() == openChar) {
                            stack.pop();
                            continue main;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }

        if (!stack.isEmpty()) {
            return false;
        }

        return result;
    }
}
