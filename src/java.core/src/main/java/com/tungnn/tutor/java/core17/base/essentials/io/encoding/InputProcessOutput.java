package com.tungnn.tutor.java.core17.base.essentials.io.encoding;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

public class InputProcessOutput {

    public static void main(String[] args) {
        String filename = "shift_jis_encoded_file.txt";

        // Input
        String pathImport = "data/import/" + filename;
        File file = new File(pathImport);
        byte[] bytes = new byte[(int) file.length()];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            bis.read(bytes);
            System.out.println(Arrays.toString(bytes));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Process
        byte[] bytesHalf1 = Arrays.copyOfRange(bytes, 0, bytes.length / 2);
        byte[] bytesHalf2 = Arrays.copyOfRange(bytes, bytes.length / 2, bytes.length);
        System.out.print(Arrays.toString(bytesHalf1));
        System.out.print(Arrays.toString(bytesHalf2));
        System.out.println();

        String s1 = new String(bytesHalf1, Charset.forName("SHIFT_JIS"));
        String s2 = new String(bytesHalf2, Charset.forName("SHIFT_JIS"));
        String output = s1 + s2;
        System.out.println(output);

        // Output
        String pathExport = "data/export/" + filename;
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pathExport))) {
            byte[] outputBytes = output.getBytes(Charset.forName("SHIFT_JIS"));
            System.out.println(Arrays.toString(outputBytes));
            bos.write(outputBytes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
