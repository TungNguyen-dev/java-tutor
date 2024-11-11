package com.tungnn.tutor.java.core17.enthuware.test04;

import java.nio.file.Path;

public class Q11 {

    public static void main(String[] args) {
        Path path = Path.of("/a/b/c");
        System.out.println(path.subpath(0, 1));
        System.out.println(path.getName(0));
    }
}
