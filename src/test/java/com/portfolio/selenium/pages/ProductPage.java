package com.portfolio.selenium.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private final By addToCartBtn = By.cssSelector("a.btn.btn-success.btn-lg");
  private final By cartLink = By.id("cartur");
  private final By nameHdr = By.cssSelector("div#tbodyid h2.name");

  public ProductPage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver; this.wait = wait;
  }

  public String productName() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(nameHdr)).getText().trim();
  }

  public void addToCartAcceptAlert() {
    wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn)).click();
    new WebDriverWait(driver, java.time.Duration.ofSeconds(10))
      .ignoring(NoAlertPresentException.class)
      .until(d -> {
        try { d.switchTo().alert().accept(); return true; } catch (NoAlertPresentException e) { return false; }
      });
  }

  public void goToCart() {
    driver.findElement(cartLink).click();
  }
}
