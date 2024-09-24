package tungnn.tutor.java.core17.enthuware.test5;

public class Q27 {

    public static void main(String[] args) {
        for (var x : System.getProperties().entrySet()) {
            var m = x.getKey();
            System.out.println(m);
        }
    }
}
