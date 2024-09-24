package tungnn.tutor.java.core17.base.decision;

public class DemoSwitchChar {

    public static void main(String[] args) {
        char c = 'A';

        switch (c) {
            case 'A':
                System.out.println("A");
                break;
            case 70:
                System.out.println("B");
                break;
            default:
                System.out.println("Unknown");
                break;
        }
    }
}
