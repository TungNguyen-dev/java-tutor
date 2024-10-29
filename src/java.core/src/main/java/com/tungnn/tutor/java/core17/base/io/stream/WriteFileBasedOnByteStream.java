package com.tungnn.tutor.java.core17.base.io.stream;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class WriteFileBasedOnByteStream {

    public static void main(String[] args) {
        List<?> list = new ArrayList<>();
        String target = "output.txt";

        try (BufferedOutputStream os = new BufferedOutputStream(
            new FileOutputStream(target))) {
            for (var obj : list) {
                os.write(obj.toString().getBytes());
                os.write('\n');
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
