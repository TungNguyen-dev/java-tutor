package com.tungnn.tutor.java.core17.enthuware.test16;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;

public class Q05 {

    public static void main(String[] args) {
        try (
            InputStream is = new FileInputStream("test16.txt");

            Reader reader = new InputStreamReader(is);

            DataInputStream din = new DataInputStream(is);

            ObjectInputStream ois = new ObjectInputStream(is);
        ) {
            // Reading
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
