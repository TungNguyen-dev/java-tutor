package tungnn.tutor.java.selenium;

public enum ProfileCategory {
  GENERIC("https://www.google.com"),
  COURSERA("https://www.coursera.org"),
  YOUTUBE("https://www.youtube.com"),
  FACEBOOK("https://www.facebook.com");

  private final String url;

  ProfileCategory(String url) {
    this.url = url;
  }

  public String url() {
    return this.url;
  }
}
