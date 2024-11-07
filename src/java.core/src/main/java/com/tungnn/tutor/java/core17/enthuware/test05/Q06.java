package com.tungnn.tutor.java.core17.enthuware.test05;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Q06 {

    public static void main(String[] args) {
        String path = "storage/random_access_file.txt";
        try (RandomAccessFile raf = new RandomAccessFile(path, "rws")) {
            System.out.println("Pointer : " + raf.getFilePointer());
            System.out.println("Length  : " + raf.length());

            System.out.println("Read    : " + (char) raf.read());
            System.out.println("Pointer : " + raf.getFilePointer());

            raf.seek(raf.length());
            System.out.println("Seek    : " + raf.getFilePointer());

            raf.write(10);
            System.out.println("Write   : " + raf.getFilePointer());

            raf.writeBytes("A");
            raf.writeChars("A");
            raf.writeUTF("A");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
