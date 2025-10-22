package com.portfolio.selenium.tests;

import com.portfolio.selenium.core.BaseTest;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
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
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", next);
  }

  private void goPrev() {
    WebElement prev = driver.findElement(By.id("prev2"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", prev);
  }

  private void waitGridNotEmpty(Duration timeout) {
    new WebDriverWait(driver, timeout)
        .until(d -> !d.findElements(By.cssSelector("#tbodyid .card-title a")).isEmpty());
  }

  @Test
  public void next_and_prev_change_catalog_items() {
    driver.navigate().to(baseUrl + "/index.html");
    waitGridNotEmpty(Duration.ofSeconds(25));

    List<String> page1 = readNames();
    Assertions.assertThat(page1).isNotEmpty();

    // Keep a handle to detect DOM refresh
    WebElement firstBefore = driver.findElement(By.cssSelector("#tbodyid .card-title a"));

    // Next →
    goNext();
    new WebDriverWait(driver, Duration.ofSeconds(25)).until(ExpectedConditions.stalenessOf(firstBefore));
    waitGridNotEmpty(Duration.ofSeconds(25));
    List<String> page2 = readNames();

    // If the set didn’t change (edge), try one extra Next
    if (new LinkedHashSet<>(page2).equals(new LinkedHashSet<>(page1))) {
      firstBefore = driver.findElement(By.cssSelector("#tbodyid .card-title a"));
      goNext();
      new WebDriverWait(driver, Duration.ofSeconds(25)).until(ExpectedConditions.stalenessOf(firstBefore));
      waitGridNotEmpty(Duration.ofSeconds(25));
      page2 = readNames();
    }

    Assertions.assertThat(new LinkedHashSet<>(page2))
        .as("page2 differs from page1")
        .isNotEqualTo(new LinkedHashSet<>(page1));

    // Prev ← (back toward original set)
    WebElement anyBeforePrev = driver.findElement(By.cssSelector("#tbodyid .card-title a"));
    goPrev();
    new WebDriverWait(driver, Duration.ofSeconds(25)).until(ExpectedConditions.stalenessOf(anyBeforePrev));
    waitGridNotEmpty(Duration.ofSeconds(25));
    List<String> page1Again = readNames();

    // If we still look like page2 (rare), try one more Prev
    if (new LinkedHashSet<>(page1Again).equals(new LinkedHashSet<>(page2))) {
      anyBeforePrev = driver.findElement(By.cssSelector("#tbodyid .card-title a"));
      goPrev();
      new WebDriverWait(driver, Duration.ofSeconds(25)).until(ExpectedConditions.stalenessOf(anyBeforePrev));
      waitGridNotEmpty(Duration.ofSeconds(25));
      page1Again = readNames();
    }

    // Robust check: after Prev we are mostly back to original items
    Set<String> s1 = new LinkedHashSet<>(page1);
    Set<String> s2 = new LinkedHashSet<>(page2);
    Set<String> sPrev = new LinkedHashSet<>(page1Again);

    int overlap = (int) sPrev.stream().filter(s1::contains).count();
    int expectedAtLeast = Math.max(1, Math.min(s1.size(), sPrev.size()) - 2); // allow up to 2 rotations
    Assertions.assertThat(overlap)
        .as("prev returns mostly original set")
        .isGreaterThanOrEqualTo(expectedAtLeast);

    // And we’re not still on page2
    Assertions.assertThat(sPrev)
        .as("prev should not equal page2")
        .isNotEqualTo(s2);
  }
}
