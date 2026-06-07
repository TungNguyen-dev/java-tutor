package tungnn.tutor.java.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import tungnn.tutor.java.selenium.element.Element;
import tungnn.tutor.java.selenium.page.crawler.PageCrawlResult;
import tungnn.tutor.java.selenium.page.crawler.PageCrawler;

import java.time.Duration;

public class JavaJLSPage extends BasePage implements PageCrawler {

  public JavaJLSPage(WebDriver driver) {
    super(driver);
  }

  @Override
  protected Duration timeout() {
    return Duration.ofSeconds(30);
  }

  @Override
  protected String homeUrl() {
    return "https://docs.oracle.com/javase/specs/jls/se26/html/index.html";
  }

  @Override
  public PageCrawlResult crawl(String url) {
    navigateTo(url);

    var id = "";
    if (url.contains("#")) {
      id = url.split("#")[1];
    } else {
      id = url.substring(url.lastIndexOf('/') + 1).replace(".html", "");
    }

    var root = $(By.id(id));

    return new PageCrawlResult(url, getTitle(root), getContent(root, id));
  }

  private String getTitle(Element root) {
    return root.$(By.className("titlepage")).text();
  }

  private String getContent(Element root, String id) {
    var paragraphs = root.$$(By.cssSelector("p[id^='" + id + "']"));

    var content = new StringBuilder();
    for (var paragraph : paragraphs) {
      content.append(paragraph.text()).append("\n\n");
    }

    return content.toString();
  }
}
