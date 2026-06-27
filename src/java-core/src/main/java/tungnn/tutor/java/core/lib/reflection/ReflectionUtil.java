package tungnn.tutor.java.core.lib.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class ReflectionUtil {

  private ReflectionUtil() {}

  public static List<Field> getInstanceFields(Class<?> clazz) {
    Objects.requireNonNull(clazz);

    return Stream.of(clazz.getDeclaredFields())
        .filter(field -> !Modifier.isStatic(field.getModifiers()))
        .toList();
  }

  public static List<Method> getInstanceMethods(Class<?> clazz) {
    Objects.requireNonNull(clazz);

    return Stream.of(clazz.getDeclaredMethods())
        .filter(method -> !Modifier.isStatic(method.getModifiers()))
        .toList();
  }
}
