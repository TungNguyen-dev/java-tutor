package tungnn.tutor.java.core.lib.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class PropertyReflectionUtil {

  private PropertyReflectionUtil() {}

  public static List<PropertyMetadata> resolveProperties(Class<?> clazz) {
    if (clazz.isRecord()) {
      return resolveRecordProperties(clazz);
    }

    var fields = ReflectionUtil.getInstanceFields(clazz);

    var methods =
        ReflectionUtil.getInstanceMethods(clazz).stream()
            .collect(Collectors.toMap(Method::getName, Function.identity(), (a, b) -> a));

    return fields.stream()
        .map(
            field -> {
              var accessor = resolveAccessor(field, methods);
              var mutator = resolveMutator(field, methods);

              return new PropertyMetadata(
                  field, accessor, mutator, accessor != null, mutator != null);
            })
        .toList();
  }

  private static Method resolveAccessor(Field field, Map<String, Method> methods) {
    var property = capitalize(field.getName());

    for (var candidate :
        List.of(
            field.getName(),
            "get" + property,
            "is" + property,
            "has" + property,
            "should" + property)) {

      var method = methods.get(candidate);

      if (method != null
          && method.getParameterCount() == 0
          && field.getType().isAssignableFrom(method.getReturnType())) {
        return method;
      }
    }

    return null;
  }

  private static Method resolveMutator(Field field, Map<String, Method> methods) {
    var method = methods.get("set" + capitalize(field.getName()));

    if (method == null) {
      return null;
    }

    if (method.getParameterCount() != 1) {
      return null;
    }

    if (!field.getType().equals(method.getParameterTypes()[0])) {
      return null;
    }

    return method;
  }

  private static String capitalize(String value) {
    if (value.isEmpty()) {
      return value;
    }

    return Character.toUpperCase(value.charAt(0)) + value.substring(1);
  }

  private static List<PropertyMetadata> resolveRecordProperties(Class<?> clazz) {
    return Stream.of(clazz.getRecordComponents())
        .map(PropertyReflectionUtil::toPropertyMetadata)
        .toList();
  }

  private static PropertyMetadata toPropertyMetadata(RecordComponent component) {
    try {
      var field = component.getDeclaringRecord().getDeclaredField(component.getName());
      var accessor = component.getAccessor();

      return new PropertyMetadata(field, accessor, null, true, false);
    } catch (NoSuchFieldException e) {
      throw new IllegalStateException("Cannot resolve record field: " + component.getName(), e);
    }
  }
}
