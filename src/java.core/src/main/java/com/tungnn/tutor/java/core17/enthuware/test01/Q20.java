package com.tungnn.tutor.java.core17.enthuware.test01;

import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;

public class Q20 {

    public static void main(String[] args) {
        ServiceLoader<Foo> loader = ServiceLoader.load(Foo.class);

        // Way - 1
        for (Foo foo : loader) {
            System.out.println(foo);
        }

        // Way - 2
        Optional<Foo> foo = loader.findFirst();

        // Way - 3
        Iterator<Foo> iterator = loader.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}

interface Foo {
}
