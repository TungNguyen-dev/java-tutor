package tungnn.tutor.java.tool.infrastructure;

public class ObsidianWriterFactory {

  private ObsidianWriterFactory() {}

  public static ObsidianWriter getInstance() {
    return Holder.INSTANCE;
  }

  private static class Holder {
    private static final ObsidianWriter INSTANCE = new ObsidianWriter();
  }
}
