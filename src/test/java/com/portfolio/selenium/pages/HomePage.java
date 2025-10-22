package com.portfolio.selenium.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  public HomePage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  public HomePage open(String baseUrl) {
    driver.navigate().to(baseUrl + "/index.html");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tbodyid")));
    // ensure product grid is populated
    new WebDriverWait(driver, java.time.Duration.ofSeconds(20))
        .until(d -> !d.findElements(By.cssSelector("#tbodyid .card-title a")).isEmpty());
    return this;
  }

  private void clickPhonesCategory() {
    // Click the “Phones” category to force a predictable list if needed
    WebElement phones = driver.findElement(
        By.xpath("//a[contains(@class,'list-group-item') and normalize-space()='Phones']"));
    ((JavascriptExecutor)driver).executeScript("arguments[0].click();", phones);
    new WebDriverWait(driver, java.time.Duration.ofSeconds(15))
        .until(d -> !d.findElements(By.cssSelector("#tbodyid .card-title a")).isEmpty());
  }

  public void openProductByName(String name) {
    By linkBy = By.xpath("//div[@id='tbodyid']//a[contains(@class,'hrefch')][normalize-space()='" + name + "']");
    try {
      WebElement link = new WebDriverWait(driver, java.time.Duration.ofSeconds(10))
          .until(ExpectedConditions.elementToBeClickable(linkBy));
      link.click();
      return;
    } catch (TimeoutException | NoSuchElementException ignored) {
      // fall through to Phones category
    }

    // Fallback: switch to Phones category and try again
    clickPhonesCategory();
    WebElement link = new WebDriverWait(driver, java.time.Duration.ofSeconds(10))
        .until(ExpectedConditions.elementToBeClickable(linkBy));
    link.click();
  }
}
