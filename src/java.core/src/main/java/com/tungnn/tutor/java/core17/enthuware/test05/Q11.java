package com.tungnn.tutor.java.core17.enthuware.test05;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Q11 {

    public static void main(String[] args) {
        class Book {
            private final String name;
            public Book(String name, String author) {
                this.name = name;
            }
            public String getName() {
                return name;
            }
        }

        var books = new ArrayList<Book>(
            List.of(new Book("The Outsider", "Stephen King"),
                    new Book("Becoming", "Michelle Obama"),
                    new Book("Uri", "India")));
        Stream stream = books.stream();
        long count = stream.peek((b) -> System.out.println(((Book) b).getName())).count();
        System.out.println(count);
    }
}
