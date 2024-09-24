package tungnn.tutor.java.core17.base.nested_classes;

public class Demo {

    public static void main(String[] args) {
        System.out.println();

        Outer outer = new Outer();
        System.out.println(outer);

        Outer.InnerStatic innerStatic = new Outer.InnerStatic();
        System.out.println(innerStatic);

        Outer.InnerInstance innerInstance = outer.new InnerInstance();
        System.out.println(innerInstance);

        Outer.InnerRecord innerRecord = new Outer.InnerRecord(null, null);
        System.out.println(innerRecord);

        outer.innerLocal();

        outer.innerAnonymous();
    }
}
