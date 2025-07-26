package com.tungnn.tutor.java.IO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DemoFileStreamByte {

  public static void main(String[] args) throws IOException {
    Path path = Path.of("storage", "data", "data-stream.txt");

    String data = "Hello, World!";
    write(path, data);
    read(path);
  }

  public static void write(Path dst, Object data) throws IOException {
    try (var out = Files.newOutputStream(dst)) {
      out.write(data.toString().getBytes());
    }
  }

  public static void read(Path src) throws IOException {
    try (var in = Files.newInputStream(src)) {
      int b;
      while ((b = in.read()) != -1) {
        System.out.print(b + " ");
      }
    }
  }

  public static void writeWithBuffer(Path dst, Object data) throws IOException {
    try (var bos = new BufferedOutputStream(Files.newOutputStream(dst))) {
      bos.write(data.toString().getBytes());
    }
  }

  public static void readWithBuffer(Path src) throws IOException {
    try (var bis = new BufferedInputStream(Files.newInputStream(src))) {
      int b;
      while ((b = bis.read()) != -1) {
        System.out.print(b + " ");
      }
    }
  }
}
