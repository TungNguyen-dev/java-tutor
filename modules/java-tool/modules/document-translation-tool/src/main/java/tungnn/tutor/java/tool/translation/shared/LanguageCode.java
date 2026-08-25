package tungnn.tutor.java.tool.translation.shared;

public enum LanguageCode {
  VI("VN", "Vietnam"),
  JA("JP", "Japan"),
  EN("EN", "English");

  private final String countryCode;
  private final String countryName;

  LanguageCode(String countryCode, String countryName) {
    this.countryCode = countryCode;
    this.countryName = countryName;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public String getCountryName() {
    return countryName;
  }
}
