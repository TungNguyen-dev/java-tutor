package tungnn.tutor.java.core17.enthuware.test5;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Q35 {

    public static void main(String[] args) {
        var books = List.of(
            new Book("Where the Crawdads Sing", "Dalia Owens"),
            new Book("The Outsider", "Stephen King"),
            new Book("Elevation", "Stephen King"),
            new Book("Coffin from Hong Kong", "James Hadley Chase"));
        Stream<Book> bookStream = books.stream();

        Optional<Book> optional = bookStream
            .filter((b) -> b.author().equals("Stephen King"))
            .findFirst();

        System.out.println(optional.orElse(null));
    }
}

record Book(String title, String author) {
}
