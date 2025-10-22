package com.portfolio.selenium.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private final By tableRows = By.cssSelector("#tbodyid tr");
  private final By placeOrderBtn = By.cssSelector("button[data-target='#orderModal']");
  private final By modal = By.id("orderModal");
  private final By name = By.id("name");
  private final By country = By.id("country");
  private final By city = By.id("city");
  private final By card = By.id("card");
  private final By month = By.id("month");
  private final By year = By.id("year");
  private final By purchaseBtn = By.xpath("//button[text()='Purchase']");
  private final By confirmModal = By.cssSelector(".sweet-alert.showSweetAlert.visible h2");

  public CartPage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver; this.wait = wait;
  }

  public boolean hasProduct(String productName) {
    wait.until(ExpectedConditions.visibilityOfElementLocated(tableRows));
    return driver.findElements(By.cssSelector("#tbodyid tr td:nth-child(2)"))
        .stream().anyMatch(td -> productName.equals(td.getText().trim()));
  }

  public void placeOrder(String n, String ctry, String ct, String cc, String mm, String yyyy) {
    driver.findElement(placeOrderBtn).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(modal));
    driver.findElement(name).sendKeys(n);
    driver.findElement(country).sendKeys(ctry);
    driver.findElement(city).sendKeys(ct);
    driver.findElement(card).sendKeys(cc);
    driver.findElement(month).sendKeys(mm);
    driver.findElement(year).sendKeys(yyyy);
    driver.findElement(purchaseBtn).click();
  }

  public String confirmationTitle() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmModal)).getText().trim();
  }
}
