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
    return this;
  }

  public void openProductByName(String name) {
    WebElement link = driver.findElement(By.xpath("//div[@id='tbodyid']//a[contains(@class,'hrefch')][normalize-space()='" + name + "']"));
    link.click();
  }
}
