package com.portfolio.selenium.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class BaseTest {
  protected WebDriver driver;
  protected WebDriverWait wait;
  protected String baseUrl;

  @BeforeClass(alwaysRun = true)
  @Parameters({"baseUrl"})
  public void setUp(@Optional String baseUrlParam) {
    baseUrl = System.getProperty("baseUrl", baseUrlParam != null ? baseUrlParam : "https://www.demoblaze.com");
    boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

    WebDriverManager.chromedriver().setup();
    ChromeOptions opts = new ChromeOptions();
    opts.addArguments("--window-size=1366,768");
    if (headless) {
      opts.addArguments("--headless=new", "--disable-gpu");
    }

    driver = new ChromeDriver(opts);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    wait = new WebDriverWait(driver, Duration.ofSeconds(15));
  }

  @AfterMethod(alwaysRun = true)
  public void snapOnFail(ITestResult result) {
    if (!result.isSuccess() && driver != null) {
      try {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path out = Path.of("target", "screenshots", result.getTestClass().getName() + "_" + result.getMethod().getMethodName() + ".png");
        Files.createDirectories(out.getParent());
        Files.copy(src.toPath(), out);
        System.out.println("Saved screenshot: " + out.toAbsolutePath());
      } catch (Exception ignored) {}
    }
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() {
    if (driver != null) driver.quit();
  }
}
