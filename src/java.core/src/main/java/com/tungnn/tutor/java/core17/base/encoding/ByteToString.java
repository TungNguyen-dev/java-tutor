package com.tungnn.tutor.java.core17.base.encoding;

import java.nio.charset.Charset;
import java.util.Arrays;

public class ByteToString {

    public static void main(String[] args) {
        byte[] bytes = {-126, -79, -126, -22, -126, -51, 83, 104, 105, 102, 116,
                        95, 74, 73, 83, -126, -59, -113, -111, -126, -87, -126,
                        -22, -126, -67, -125, 101, -125, 76, -125, 88, -125,
                        103, -126, -59, -126, -73};
        System.out.println(Arrays.toString(bytes));

        String s;

        s = new String(bytes);
        System.out.println(s);
        System.out.println(Arrays.toString(s.getBytes()));

        s = new String(bytes, Charset.forName("SHIFT_JIS"));
        System.out.println(s);
        System.out.println(Arrays.toString(s.getBytes()));
    }
}
