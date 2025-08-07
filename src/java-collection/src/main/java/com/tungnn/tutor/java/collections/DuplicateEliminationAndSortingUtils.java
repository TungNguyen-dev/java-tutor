package com.tungnn.tutor.java.collections;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for eliminating duplicates from and sorting collections of objects based on
 * composite unique key fields. Classes intended to use this utility must be annotated with the
 * {@link DuplicateEliminationAndSortingKey} annotation, specifying the fields that determine
 * uniqueness and sorting order.
 */
public class DuplicateEliminationAndSortingUtils {

  /**
   * Eliminates duplicates from a collection of objects and sorts them based on the fields specified
   * in the {@link DuplicateEliminationAndSortingKey} annotation.
   *
   * @param input A collection of objects to process. Can be {@code null} or empty.
   * @return A sorted list of unique objects. If the input collection is {@code null} or empty, an
   *     empty list is returned.
   * @throws IllegalArgumentException If the input objects' class is not annotated with {@link
   *     DuplicateEliminationAndSortingKey}.
   */
  public static List<Object> eliminateDuplicationAndSort(Collection<?> input) {
    if (input == null || input.isEmpty()) {
      return new ArrayList<>();
    }

    String[] fields = obtainKeyFieldsForDuplicateEliminationAndSorting(input.iterator().next());
    Set<EqualsWrapper> set = input.stream().map(EqualsWrapper::new).collect(Collectors.toSet());
    Comparator<EqualsWrapper> comparator = buildMultiFieldComparator(fields, true);

    return set.stream()
        .sorted(comparator)
        .map(EqualsWrapper::getOriginal)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves the field names specified in the {@link DuplicateEliminationAndSortingKey} annotation
   * of the given object's class.
   *
   * @param obj The object whose annotated fields are to be identified.
   * @return An array of field names specified in the annotation.
   * @throws IllegalArgumentException If the object's class is not annotated with {@link
   *     DuplicateEliminationAndSortingKey}.
   */
  private static String[] obtainKeyFieldsForDuplicateEliminationAndSorting(Object obj) {
    Class<?> clazz = obj.getClass();
    DuplicateEliminationAndSortingKey annotation =
        clazz.getAnnotation(DuplicateEliminationAndSortingKey.class);

    if (annotation == null) {
      throw new IllegalArgumentException(
          "Class must be annotated with @DuplicateEliminationAndSortingKey");
    }

    return annotation.fields();
  }

  /**
   * Builds a comparator to compare objects based on multiple fields. The fields that define the
   * comparison logic must be specified in the {@link DuplicateEliminationAndSortingKey} annotation.
   *
   * @param fields The array of field names to use for comparison.
   * @param nullsLast Whether {@code null} values should appear last ({@code true}) or first ({@code
   *     false}) in the sorting order.
   * @return A comparator for comparing objects wrapped in {@link EqualsWrapper}.
   */
  public static Comparator<EqualsWrapper> buildMultiFieldComparator(
      String[] fields, boolean nullsLast) {
    Comparator<EqualsWrapper> comparator = comparingByIndex(0, nullsLast);

    for (int i = 1; i < fields.length; i++) {
      comparator = comparator.thenComparing(comparingByIndex(i, nullsLast));
    }

    return comparator;
  }

  /**
   * Creates a comparator for comparing {@link EqualsWrapper} objects based on a specific index of
   * the field.
   *
   * @param index The index of the field to use for comparison.
   * @param nullsLast Whether {@code null} values should appear last ({@code true}) or first ({@code
   *     false}) in the sorting order.
   * @return A comparator for {@link EqualsWrapper} objects.
   */
  private static Comparator<EqualsWrapper> comparingByIndex(int index, boolean nullsLast) {
    Function<EqualsWrapper, Comparable<Object>> keyExtractor =
        wrapper -> {
          Object value = wrapper.keyValues.get(index);
          return value == null ? null : safeCastToComparable(value);
        };

    return Comparator.comparing(
        keyExtractor,
        nullsLast
            ? Comparator.nullsLast(Comparator.naturalOrder())
            : Comparator.nullsFirst(Comparator.naturalOrder()));
  }

  /**
   * Safely casts an object to {@link Comparable}. Ensures that the object implements {@link
   * Comparable} before performing the cast.
   *
   * @param value The object to cast.
   * @return The object as a {@link Comparable}.
   * @throws ClassCastException If the object does not implement {@link Comparable}.
   */
  @SuppressWarnings("unchecked")
  private static Comparable<Object> safeCastToComparable(Object value) {
    if (value instanceof Comparable) {
      return (Comparable<Object>) value;
    } else {
      throw new ClassCastException("Field value must implement Comparable: " + value);
    }
  }

  /**
   * Class-level annotation to define composite unique key fields for objects. This annotation
   * specifies the names of the fields used to determine the uniqueness and sorting order of objects
   * in collections.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  public @interface DuplicateEliminationAndSortingKey {
    /**
     * Names of the fields used for determining uniqueness and sorting order of objects.
     *
     * @return An array of field names.
     */
    String[] fields();
  }

  /**
   * Wrapper class to encapsulate an object and its key values, used for equality comparison and
   * sorting purposes.
   */
  public static class EqualsWrapper {
    private final Object original;
    private final List<Object> keyValues;

    /**
     * Constructs an {@link EqualsWrapper} for the given object and extracts its key values based on
     * the fields specified in the {@link DuplicateEliminationAndSortingKey} annotation.
     *
     * @param obj The object to wrap.
     * @throws IllegalArgumentException If the object's class is not annotated with {@link
     *     DuplicateEliminationAndSortingKey}.
     * @throws RuntimeException If a field specified in the annotation cannot be accessed.
     */
    public EqualsWrapper(Object obj) {
      this.original = obj;

      Class<?> clazz = obj.getClass();
      DuplicateEliminationAndSortingKey annotation =
          clazz.getAnnotation(DuplicateEliminationAndSortingKey.class);

      if (annotation == null) {
        throw new IllegalArgumentException(
            "Class must be annotated with @DuplicateEliminationAndSortingKey");
      }

      String[] fields = annotation.fields();

      keyValues =
          Arrays.stream(fields)
              .map(
                  fieldName -> {
                    try {
                      Field field = clazz.getDeclaredField(fieldName);
                      field.setAccessible(true);
                      return field.get(obj);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                      throw new RuntimeException("Error accessing field: " + fieldName, e);
                    }
                  })
              .collect(Collectors.toList());
    }

    /**
     * Retrieves the original object wrapped by this {@link EqualsWrapper}.
     *
     * @return The original object.
     */
    public Object getOriginal() {
      return original;
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof EqualsWrapper that)) return false;
      return Objects.equals(this.keyValues, that.keyValues);
    }

    @Override
    public int hashCode() {
      return Objects.hash(keyValues);
    }
  }
}
