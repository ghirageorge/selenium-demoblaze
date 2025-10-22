package com.portfolio.selenium.tests;

import com.portfolio.selenium.core.BaseTest;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CatalogPaginationTest extends BaseTest {

  private List<String> readNames() {
    var cards = driver.findElements(By.cssSelector("#tbodyid .card-title a"));
    return cards.stream().map(e -> e.getText().trim()).collect(Collectors.toList());
  }

  private void goNext() {
    WebElement next = driver.findElement(By.id("next2"));
    ((JavascriptExecutor)driver).executeScript("arguments[0].click();", next);
  }

  private void goPrev() {
    WebElement prev = driver.findElement(By.id("prev2"));
    ((JavascriptExecutor)driver).executeScript("arguments[0].click();", prev);
  }

  @Test
  public void next_and_prev_change_catalog_items() {
  driver.navigate().to(baseUrl + "/index.html");
  new WebDriverWait(driver, java.time.Duration.ofSeconds(20))
      .until(d -> !d.findElements(By.cssSelector("#tbodyid .card-title a")).isEmpty());

  List<String> page1 = readNames();
  Assertions.assertThat(page1).isNotEmpty();

  // Next →
  goNext();
  new WebDriverWait(driver, java.time.Duration.ofSeconds(15))
      .until(d -> {
        List<String> probe = readNames();
        return !probe.isEmpty() && !new LinkedHashSet<>(probe).equals(new LinkedHashSet<>(page1));
      });
  List<String> page2 = readNames();
  Assertions.assertThat(new LinkedHashSet<>(page2)).isNotEqualTo(new LinkedHashSet<>(page1));

  // Prev ←
  goPrev();
  new WebDriverWait(driver, java.time.Duration.ofSeconds(15))
      .until(d -> new LinkedHashSet<>(readNames()).equals(new LinkedHashSet<>(page1)));
}
}
