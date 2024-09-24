package tungnn.tutor.java.core17.base.control_flow.branching;

public class DemoLabel {

    public static void main(String[] args) {
        loopLabel:
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == j) {
                    break loopLabel;
                }
            }
        }


    }
}
