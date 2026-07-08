package tungnn.tutor.java.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import tungnn.tutor.java.core.lib.reflection.PropertyMetadata;
import tungnn.tutor.java.core.lib.reflection.PropertyReflectionUtil;
import tungnn.tutor.java.core.lib.reflection.ReflectionUtil;

public final class CsvUtil {

  private CsvUtil() {}

  // ==========================================
  // READ METHODS
  // ==========================================

  public static <E> List<E> read(CSVFormat csvFormat, Path path, Class<E> clazz) {
    try (var stream = readAsStream(csvFormat, path, clazz)) {
      return stream.toList();
    } catch (Exception e) {
      throw new RuntimeException("Failed to read CSV: " + path, e);
    }
  }

  public static <E> Stream<E> readAsStream(CSVFormat csvFormat, Path path, Class<E> clazz) {
    BufferedReader reader = null;
    CSVParser parser = null;
    try {
      reader = Files.newBufferedReader(path);
      parser = csvFormat.parse(reader);

      var propertiesMetadata = PropertyReflectionUtil.resolveProperties(clazz);

      final var finalReader = reader;
      final var finalParser = parser;

      return parser.stream()
          .map(csvRecord -> mapRecordToEntity(csvRecord, propertiesMetadata, clazz))
          .onClose(() -> closeResources(finalParser, finalReader));

    } catch (Exception e) {
      closeResources(parser, reader);
      throw new RuntimeException("Failed to read CSV as stream: " + path, e);
    }
  }

  // ==========================================
  // WRITE METHODS
  // ==========================================

  public static <E> void write(CSVFormat csvFormat, Path path, Collection<E> data, Class<E> clazz) {
    try (var stream = data.stream()) {
      writeStream(csvFormat, path, stream, clazz);
    } catch (Exception e) {
      throw new RuntimeException("Failed to write CSV: " + path, e);
    }
  }

  public static <E> void writeStream(
      CSVFormat csvFormat, Path path, Stream<E> data, Class<E> clazz) {
    try (BufferedWriter writer = Files.newBufferedWriter(path);
        CSVPrinter printer = csvFormat.print(writer)) {

      var properties = PropertyReflectionUtil.resolveProperties(clazz);

      String[] headers = properties.stream().map(p -> p.field().getName()).toArray(String[]::new);
      printer.printRecord((Object[]) headers);

      var accessors = resolveAccessors(properties);

      data.forEach(
          element -> {
            Object[] values = new Object[headers.length];
            for (int i = 0; i < headers.length; i++) {
              values[i] = accessors[i].apply(element);
            }
            try {
              printer.printRecord(values);
            } catch (Exception ex) {
              throw new RuntimeException("Failed to write record", ex);
            }
          });

      printer.flush();

    } catch (Exception e) {
      throw new RuntimeException("Failed to write CSV as stream: " + path, e);
    }
  }

  // ==========================================
  // CORE MAPPING & REFLECTION LOGIC
  // ==========================================

  private static <E> E mapRecordToEntity(
      CSVRecord csvRecord, List<PropertyMetadata> properties, Class<E> clazz) {
    Object[] args = new Object[properties.size()];
    for (int i = 0; i < properties.size(); i++) {
      var prop = properties.get(i);
      args[i] = readValue(csvRecord, prop.field().getName(), prop.field().getType());
    }
    return ReflectionUtil.newInstance(clazz, args);
  }

  @SuppressWarnings("unchecked")
  private static <E> Function<E, Object>[] resolveAccessors(List<PropertyMetadata> properties) {
    Function<E, Object>[] accessors = new Function[properties.size()];
    for (int i = 0; i < properties.size(); i++) {
      var prop = properties.get(i);

      if (prop.readable() && prop.accessor() != null) {
        var accessorMethod = prop.accessor();
        accessors[i] =
            e -> {
              try {
                if (!accessorMethod.canAccess(e)) accessorMethod.setAccessible(true);
                return accessorMethod.invoke(e);
              } catch (Exception ex) {
                throw new RuntimeException(ex);
              }
            };
      } else {
        var field = prop.field();
        accessors[i] =
            e -> {
              try {
                if (!field.canAccess(e)) field.setAccessible(true);
                return field.get(e);
              } catch (Exception ex) {
                throw new RuntimeException(ex);
              }
            };
      }
    }
    return accessors;
  }

  private static Object readValue(CSVRecord record, String name, Class<?> type) {
    String raw =
        record.isMapped(name)
            ? record.get(name)
            : record.isMapped(name.toLowerCase(Locale.ROOT))
                ? record.get(name.toLowerCase(Locale.ROOT))
                : null;

    if (raw == null) {
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

  private static void closeResources(AutoCloseable... closeables) {
    for (var closeable : closeables) {
      if (closeable != null) {
        try {
          closeable.close();
        } catch (Exception _) {
        }
      }
    }
  }
}
