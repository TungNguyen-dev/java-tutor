package com.tungnn.tutor.java.core17.base.essentials.exception;

import java.io.IOException;

public class SuppressedException {

    public static void main(String[] args) {
        class Device implements AutoCloseable {
            private String name;

            public Device(String name) throws IOException {
                this.name = name;
                if ("D2".equals(name)) throw new IOException("Error creating " + name);
                System.out.println("Device " + name + " is created successfully.");
            }

            @Override
            public void close() throws Exception {
                System.out.println("Closing device " + name);
                throw new Exception("Error closing " + name);
            }
        }

        try (
            Device d1 = new Device("D1");
            Device d2 = new Device("D2") // This will throw IOException
        ) {
            throw new Exception("test"); // This line is never reached
        } catch (Exception e) {
            System.out.println("Caught exception: " + e);
            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("Suppressed exception: " + suppressed);
            }
        }
    }
}
