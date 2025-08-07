package com.tungnn.tutor.java.IO.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;

public class DemoChannelBuffer {

  public static void main(String[] args) throws IOException {
    Path path = Path.of("storage", "data", "data-channel.txt");
    String data = "data for channel write-read";
    write(path, data);
    read(path);
  }

  public static void write(Path dst, Object data) throws IOException {
    var writeOptionSet = Set.of(StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    try (var channel = FileChannel.open(dst, writeOptionSet)) {
      ByteBuffer buffer = ByteBuffer.wrap(data.toString().getBytes());
      int bytesWritten;
      bytesWritten = channel.write(buffer);
      System.out.println(bytesWritten);
    }
  }

  public static void read(Path src) throws IOException {
    try (var channel = FileChannel.open(src)) {
      ByteBuffer buffer = ByteBuffer.allocate(8 * 1024);
      int bytesRead;
      do {
        buffer.clear();
        bytesRead = channel.read(buffer);

        buffer.flip();
        while (buffer.hasRemaining()) {
          System.out.print(buffer.get() + " ");
        }
      } while (bytesRead != -1 && bytesRead != 0);
    }
  }
}
