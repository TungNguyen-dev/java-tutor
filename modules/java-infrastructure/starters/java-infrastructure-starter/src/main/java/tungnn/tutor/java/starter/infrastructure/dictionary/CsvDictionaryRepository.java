package tungnn.tutor.java.starter.infrastructure.dictionary;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import org.apache.commons.csv.CSVFormat;
import tungnn.tutor.java.csv.CsvUtil;

public class CsvDictionaryRepository implements DictionaryRepository {

  private final Path path;
  private final CSVFormat csvFormat;

  public CsvDictionaryRepository(Path path) {
    this.path = Objects.requireNonNull(path);

    this.csvFormat =
        CSVFormat.DEFAULT
            .builder()
            .setHeader("id", "original", "translation")
            .setSkipHeaderRecord(false)
            .build();

    initFileIfNotExists();
  }

  private void initFileIfNotExists() {
    try {
      if (Files.notExists(path)) {
        Files.createDirectories(path.getParent());
        Files.createFile(path);

        // write empty with header
        CsvUtil.write(csvFormat, path, List.of(), DictionaryItem.class);
      }
    } catch (Exception e) {
      throw new RuntimeException("Cannot initialize CSV file", e);
    }
  }

  @Override
  public List<DictionaryItem> findAll() {
    return CsvUtil.read(csvFormat, path, DictionaryItem.class);
  }

  @Override
  public void saveAll(List<DictionaryItem> items) {
    Map<String, DictionaryItem> merged = new LinkedHashMap<>();

    // existing
    for (var item : findAll()) {
      merged.put(item.id(), item);
    }

    // override updated
    for (var item : items) {
      merged.put(item.id(), item);
    }

    CsvUtil.write(csvFormat, path, merged.values(), DictionaryItem.class);
  }

  @Override
  public void save(DictionaryItem item) {
    // Read -> add -> write (simple but O(n))
    List<DictionaryItem> all = new ArrayList<>(findAll());
    all.add(item);

    CsvUtil.write(csvFormat, path, all, DictionaryItem.class);
  }
}
