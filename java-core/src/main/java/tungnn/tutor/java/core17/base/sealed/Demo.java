package tungnn.tutor.java.core17.base.sealed;

public class Demo {
}

sealed interface InterfaceSealed permits InterfaceNonSealed {

}

non-sealed interface InterfaceNonSealed extends InterfaceSealed {

}
