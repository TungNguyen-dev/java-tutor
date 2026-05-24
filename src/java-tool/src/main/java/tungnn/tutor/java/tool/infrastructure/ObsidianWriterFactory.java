package tungnn.tutor.java.tool.infrastructure;

public class ObsidianWriterFactory {

  private ObsidianWriterFactory() {}

  private static class Holder {
    private static final ObsidianWriter INSTANCE = new ObsidianWriter();
  }

  public static ObsidianWriter getInstance() {
    return Holder.INSTANCE;
  }
}
