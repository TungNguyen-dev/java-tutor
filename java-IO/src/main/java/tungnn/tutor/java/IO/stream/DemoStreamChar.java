package tungnn.tutor.java.IO.stream;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class DemoStreamChar {

  public static void main(String[] args) throws IOException {
    Path path = Path.of("storage", "data", "data-char.txt");

    String data = "Hello, World!";
    write(path, data, StandardCharsets.UTF_8);
    read(path, StandardCharsets.UTF_8);
  }

  public static void write(Path dst, Object data, Charset charset) throws IOException {
    try (var writer = new FileWriter(dst.toFile(), charset)) {
      writer.write(data.toString());
    }
  }

  public static void read(Path src, Charset charset) throws IOException {
    try (var reader = new FileReader(src.toFile(), charset)) {
      int c;
      while ((c = reader.read()) != -1) {
        System.out.print((char) c + "");
      }
    }
  }

  public static void writeWithBuffer(Path dst, Object data, Charset charset) throws IOException {
    try (var buffWriter = Files.newBufferedWriter(dst, charset)) {
      buffWriter.write(data.toString());
    }
  }

  public static void readWithBuffer(Path src, Charset charset) throws IOException {
    try (var buffReader = Files.newBufferedReader(src, charset)) {
      int c;
      while ((c = buffReader.read()) != -1) {
        System.out.print((char) c + "");
      }
    }
  }
}
