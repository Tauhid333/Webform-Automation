import com.github.javafaker.Faker;
import dev.failsafe.internal.util.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DigitalUnitWebformAutomation {
    WebDriver driver;
    @BeforeAll
    public void setup(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headed");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

    }

    @DisplayName("Successful form submission")
    @Test
    public void webform_submission(){
        driver.get("https://www.digitalunite.com/practice-webform-learners");
        Faker faker = new Faker();
        String name   = faker.name().firstName() + " " + faker.name().lastName();
        String number = faker.phoneNumber().cellPhone().replace("-","");
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        String date_today = today.format(formatter);
        String email = faker.internet().emailAddress();
        String profile ="Hello I am a SQA Engineer.";
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        List<WebElement> formControls = driver.findElements(By.className("form-control"));
        WebElement uploadElement = driver.findElement(By.id("edit-uploadocument-upload"));
        formControls.get(0).sendKeys(name);
        formControls.get(1).sendKeys(number);
        formControls.get(2).sendKeys(date_today);
        formControls.get(3).sendKeys(email);
        formControls.get(4).sendKeys(profile);
        scroll();
        uploadElement.sendKeys("D:/Assignment 5/f5.png");
        WebElement checkbox = driver.findElement(By.id("edit-age"));
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", checkbox);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("file")));
        WebElement submitBtn = driver.findElement(By.id("edit-submit"));
        executor.executeScript("arguments[0].click();", submitBtn);

        WebElement headerTextElem = driver.findElement(By.xpath("//h1[text() = \"Thank you for your submission!\"]"));
        wait.until(
                ExpectedConditions.visibilityOf(headerTextElem));
        String textTitle = headerTextElem.getText();
        Assertions.assertTrue(textTitle.contains("Thank you for your submission!"));
    }

    public void scroll(){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    @AfterAll
    public void closeBrowser(){
//        driver.quit();
    }
}
