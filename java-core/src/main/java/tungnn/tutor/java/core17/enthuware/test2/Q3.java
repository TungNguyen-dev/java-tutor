package tungnn.tutor.java.core17.enthuware.test2;

public record Q3(Integer id, String name) {

    public Q3 {
        id *= 2;
    }

    public Q3() {
        this(null, null);
    }
}
