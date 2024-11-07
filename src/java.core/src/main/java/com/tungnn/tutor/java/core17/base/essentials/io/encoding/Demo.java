package com.tungnn.tutor.java.core17.base.essentials.io.encoding;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo {

    public static void main(String[] args) {
        String fileName = "shift_jis_encoded_file.txt";
        String path = "data/import/" + fileName;
        File file = new File(path);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] fileBytes = new byte[(int) file.length()];

            if (bis.read(fileBytes) != -1) {
                System.out.println(Arrays.toString(fileBytes));
                System.out.println(fileBytes.length);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void write() {
        String text
            = "これはShift_JISで書かれたテキストです"; // Some Japanese text
        String fileName
            = "shift_jis_encoded_file.txt";  // Name of the output file

        try (Writer writer = new OutputStreamWriter(
            new FileOutputStream("data/import/" + fileName), "Shift_JIS")) {
            writer.write(text);
            System.out.println(
                "File written in Shift_JIS encoding: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void read() {
        String fileName = "shift_jis_encoded_file.txt";

        try (BufferedInputStream bis = new BufferedInputStream( new FileInputStream("data/import/" + fileName))) {

            List<Integer> bytes = new ArrayList<>();
            int c;
            while ((c = bis.read()) != -1) {
                bytes.add(c);
                System.out.print((char) c);
            }
            System.out.println();
            System.out.println(bytes);
            System.out.println(bytes.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(
                new FileInputStream("data/import/" + fileName), "Shift_JIS"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                System.out.println(line.getBytes().length);
                System.out.println(Arrays.toString(line.getBytes()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
