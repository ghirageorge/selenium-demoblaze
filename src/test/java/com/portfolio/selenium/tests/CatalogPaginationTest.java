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
    new WebDriverWait(driver, java.time.Duration.ofSeconds(10))
        .until(ExpectedConditions.visibilityOfElementLocated(By.id("tbodyid")));

    List<String> page1 = readNames();
    Assertions.assertThat(page1).isNotEmpty();

    goNext();
    new WebDriverWait(driver, java.time.Duration.ofSeconds(10))
      .until(d -> {
        List<String> page2Probe = readNames();
        return !page2Probe.isEmpty() && !new LinkedHashSet<>(page2Probe).equals(new LinkedHashSet<>(page1));
      });

    List<String> page2 = readNames();
    Set<String> s1 = new LinkedHashSet<>(page1);
    Set<String> s2 = new LinkedHashSet<>(page2);
    Assertions.assertThat(s2).as("page2 != page1").isNotEqualTo(s1);

    goPrev();
    new WebDriverWait(driver, java.time.Duration.ofSeconds(10))
        .until(d -> new LinkedHashSet<>(readNames()).equals(s1));
  }
}
