package tungnn.tutor.java.selenium.demo;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tungnn.tutor.java.selenium.SeleniumUtil;

public class Demo {

  static void main() {
    // 1. Khởi tạo và mở trình duyệt Chrome
    WebDriver driver = SeleniumUtil.createChromeDriver();

    // 2. Điều hướng trình duyệt đến trang web được chỉ định
    driver.get("https://www.selenium.dev/selenium/web/web-form.html");

    // 3. Lấy tiêu đề (Title) của trang hiện tại (chưa lưu hoặc in ra)
    driver.getTitle();

    // 4. Cấu hình chờ ngầm định (Implicit Wait) tối đa 500ms khi tìm kiếm các phần tử
    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

    // 5. Tìm ô nhập liệu (text box) theo thuộc tính name và nút submit theo CSS selector
    WebElement textBox = driver.findElement(By.name("my-text"));
    WebElement submitButton = driver.findElement(By.cssSelector("button"));

    // 6. Nhập chuỗi "Selenium" vào ô text box và click vào nút submit
    textBox.sendKeys("Selenium");
    submitButton.click();

    // 7. Tìm phần tử hiển thị thông báo kết quả bằng ID "message"
    WebElement message = driver.findElement(By.id("message"));

    // 8. Lấy nội dung văn bản bên trong phần tử thông báo (chưa lưu hoặc in ra)
    message.getText();

    // 9. Đóng toàn bộ các cửa sổ trình duyệt và kết thúc phiên làm việc (session)
    driver.quit();
  }
}
