package tungnn.tutor.java.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CsvUtil {

  private CsvUtil() {}

  public static <E> List<E> read(CSVFormat csvFormat, Path path, Class<E> clazz) {

    try (BufferedReader reader = Files.newBufferedReader(path);
        CSVParser parser = csvFormat.parse(reader)) {

      var metadata = resolveRecordMetadata(clazz);
      var constructor = metadata.constructor;
      var names = metadata.names;
      var types = metadata.types;

      return parser.stream()
          .map(
              csvRecord -> {
                Object[] args = new Object[names.length];
                for (int i = 0; i < names.length; i++) {
                  args[i] = readValue(csvRecord, names[i], types[i]);
                }
                return newInstance(constructor, args);
              })
          .collect(Collectors.toList());

    } catch (Exception e) {
      throw new RuntimeException("Failed to read CSV: " + path, e);
    }
  }

  public static <E> Stream<E> readAsStream(CSVFormat csvFormat, Path path, Class<E> clazz) {
    try {
      BufferedReader reader = Files.newBufferedReader(path);
      CSVParser parser = csvFormat.parse(reader);

      var metadata = resolveRecordMetadata(clazz);
      var constructor = metadata.constructor;
      var names = metadata.names;
      var types = metadata.types;

      return parser.stream()
          .map(
              csvRecord -> {
                Object[] args = new Object[names.length];
                for (int i = 0; i < names.length; i++) {
                  args[i] = readValue(csvRecord, names[i], types[i]);
                }
                return newInstance(constructor, args);
              })
          .onClose(
              () -> {
                try {
                  parser.close();
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
                try {
                  reader.close();
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
              });

    } catch (Exception e) {
      throw new RuntimeException("Failed to read CSV as stream: " + path, e);
    }
  }

  public static <E> void write(CSVFormat csvFormat, Path path, Collection<E> data, Class<E> clazz) {

    try (var writer = Files.newBufferedWriter(path);
        var printer = csvFormat.print(writer)) {

      var metadata = resolveRecordMetadata(clazz);
      var names = metadata.names;

      // Write header
      printer.printRecord((Object[]) names);

      var accessors = resolveAccessors(clazz);

      for (E element : data) {
        Object[] values = new Object[names.length];
        for (int i = 0; i < names.length; i++) {
          values[i] = accessors[i].apply(element);
        }
        printer.printRecord(values);
      }

    } catch (Exception e) {
      throw new RuntimeException("Failed to write CSV: " + path, e);
    }
  }

  public static <E> void writeAsStream(
      CSVFormat csvFormat, Path path, Stream<E> data, Class<E> clazz) {

    try (var writer = Files.newBufferedWriter(path);
        var printer = csvFormat.print(writer);
        data) {

      var metadata = resolveRecordMetadata(clazz);
      var names = metadata.names;

      // Write header
      printer.printRecord((Object[]) names);

      var accessors = resolveAccessors(clazz);

      data.forEach(
          element -> {
            Object[] values = new Object[names.length];
            for (int i = 0; i < names.length; i++) {
              values[i] = accessors[i].apply(element);
            }

            try {
              printer.printRecord(values);
            } catch (Exception e) {
              throw new RuntimeException("Failed to write record", e);
            }
          });

      printer.flush();

    } catch (Exception e) {
      throw new RuntimeException("Failed to write CSV as stream: " + path, e);
    }
  }

  // =========================
  // Record Metadata
  // =========================
  private static <E> RecordMetadata<E> resolveRecordMetadata(Class<E> clazz) {
    if (!clazz.isRecord()) {
      throw new IllegalArgumentException("Only record is supported: " + clazz);
    }

    try {
      var components = clazz.getRecordComponents();

      String[] names =
          Arrays.stream(components).map(RecordComponent::getName).toArray(String[]::new);

      Class<?>[] types =
          Arrays.stream(components).map(RecordComponent::getType).toArray(Class[]::new);

      Constructor<E> constructor = clazz.getDeclaredConstructor(types);
      constructor.setAccessible(true);

      return new RecordMetadata<>(constructor, names, types);

    } catch (Exception e) {
      throw new RuntimeException("Failed to resolve record metadata: " + clazz, e);
    }
  }

  private static <E> Function<E, Object>[] resolveAccessors(Class<E> clazz) {

    if (!clazz.isRecord()) {
      throw new IllegalArgumentException("Only record is supported: " + clazz);
    }

    var components = clazz.getRecordComponents();

    @SuppressWarnings("unchecked")
    Function<E, Object>[] accessors = new Function[components.length];

    try {
      for (int i = 0; i < components.length; i++) {
        var method = components[i].getAccessor();
        method.setAccessible(true);

        accessors[i] =
            e -> {
              try {
                return method.invoke(e);
              } catch (Exception ex) {
                throw new RuntimeException(ex);
              }
            };
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to resolve accessors", e);
    }

    return accessors;
  }

  private static <E> E newInstance(Constructor<E> constructor, Object[] args) {
    try {
      return constructor.newInstance(args);
    } catch (Exception e) {
      throw new RuntimeException("Failed to instantiate " + constructor.getDeclaringClass(), e);
    }
  }

  private static Object readValue(CSVRecord record, String name, Class<?> type) {
    String raw;

    if (record.isMapped(name)) {
      raw = record.get(name);
    } else if (record.isMapped(name.toLowerCase(Locale.ROOT))) {
      raw = record.get(name.toLowerCase(Locale.ROOT));
    } else {
      throw new IllegalArgumentException("Missing column: " + name);
    }

    return convert(raw, type);
  }

  private static Object convert(String value, Class<?> type) {
    if (value == null || value.isEmpty()) {
      if (type.isPrimitive()) {
        throw new IllegalArgumentException("Cannot map null/empty to primitive: " + type);
      }
      return null;
    }

    if (type == String.class) return value;
    if (type == int.class || type == Integer.class) return Integer.parseInt(value);
    if (type == long.class || type == Long.class) return Long.parseLong(value);
    if (type == double.class || type == Double.class) return Double.parseDouble(value);
    if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);

    throw new UnsupportedOperationException("Unsupported type: " + type);
  }

  private record RecordMetadata<E>(Constructor<E> constructor, String[] names, Class<?>[] types) {}
}
