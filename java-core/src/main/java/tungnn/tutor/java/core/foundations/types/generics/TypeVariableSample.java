package tungnn.tutor.java.core.foundations.types.generics;

public class TypeVariableSample {

  class ClassOrInterfaceType {}

  interface I1 {}

  interface I2 {}

  interface I3<T> {}

  class Foo1<T> {}

  class Foo2<T extends ClassOrInterfaceType> {}

  class Foo3<T extends ClassOrInterfaceType & I1 & I2> {}

  // class Foo4<T extends I3<String> & I3<Number>> {}
  // class Foo4<T extends I3<String> & I3<String>> {}

  class Foo5<S> {
    class Bar<T extends S> {}
  }
}
