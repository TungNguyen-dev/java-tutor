package tungnn.tutor.java.core17.enthuware.test1;

import java.io.Closeable;
import java.io.IOException;

public class Q7 {

    public static void main(String[] args) {
        try (AutoCloseable autoCloseable = new Foo();
             AutoCloseable closeable = new Bar()) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


/**
 * AutoCloseable is super type of Closeable
 */
class Foo implements AutoCloseable {

    @Override
    public void close() throws Exception {

    }
}

class Bar implements Closeable {
    @Override
    public void close() throws IOException {

    }
}
