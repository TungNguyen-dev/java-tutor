package tungnn.tutor.java.starter.infrastructure.translation.v2.shared;

public enum TranslationLanguage {
  UNKNOWN("unknown"),
  VIETNAMESE("vn"),
  ENGLISH("en"),
  JAPANESE("jp");

  private final String code;

  TranslationLanguage(String code) {
    this.code = code;
  }

  public String code() {
    return code;
  }
}
