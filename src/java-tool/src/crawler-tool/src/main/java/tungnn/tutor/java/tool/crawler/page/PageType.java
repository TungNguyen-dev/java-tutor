package tungnn.tutor.java.tool.crawler.page;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import org.openqa.selenium.WebDriver;

/**
 * Registry of supported web page types for crawling. Matches incoming URLs to their respective
 * {@link AbstractPage} implementations and handles instantiation via reflection.
 */
public enum PageType {

  /** Page implementation for YouTube content extraction. */
  YOUTUBE("youtube.com", YoutubePage.class),

  /** Page implementation for Coursera course content extraction. */
  COURSERA("coursera.org", CourseraPage.class);

  private final String domainKeyword;
  private final Class<? extends AbstractPage> pageClass;

  /**
   * Constructs a new {@code PageType} entry.
   *
   * @param domainKeyword key domain substring used for URL matching
   * @param pageClass the concrete class extending {@link AbstractPage}
   */
  PageType(String domainKeyword, Class<? extends AbstractPage> pageClass) {
    this.domainKeyword = domainKeyword;
    this.pageClass = pageClass;
  }

  /**
   * Resolves the appropriate {@code PageType} for a given URL.
   *
   * @param url the target URL to classify
   * @return an {@link Optional} containing the matched {@code PageType}, or empty if not supported
   */
  public static Optional<PageType> fromUrl(String url) {
    return Arrays.stream(values()).filter(type -> type.supports(url)).findFirst();
  }

  /**
   * Determines if the given URL matches this page type.
   *
   * @param url the target URL to check
   * @return {@code true} if the URL matches the domain keyword; {@code false} otherwise
   */
  public boolean supports(String url) {
    if (url == null || url.isBlank()) {
      return false;
    }
    try {
      URI uri = new URI(url.toLowerCase());
      String host = uri.getHost();
      return host != null && host.contains(domainKeyword);
    } catch (Exception e) {
      return url.toLowerCase().contains(domainKeyword);
    }
  }

  /**
   * Creates a new instance of the associated {@link AbstractPage} using reflection.
   *
   * @param driver the active {@link WebDriver} instance
   * @return a new instance of the matching {@link AbstractPage} subclass
   * @throws RuntimeException if instantiation fails
   */
  public AbstractPage createPageInstance(WebDriver driver) {
    try {
      Constructor<? extends AbstractPage> constructor = pageClass.getConstructor(WebDriver.class);
      return constructor.newInstance(driver);
    } catch (Exception e) {
      throw new RuntimeException(
          "Failed to instantiate Page Object for class: " + pageClass.getName(), e);
    }
  }
}
