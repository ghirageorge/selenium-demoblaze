package com.portfolio.selenium.tests;

import com.portfolio.selenium.core.BaseTest;
import com.portfolio.selenium.pages.*;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class AddToCartCheckoutTest extends BaseTest {
  @Test
  public void add_to_cart_then_checkout_success() {
    var home = new HomePage(driver, wait).open(baseUrl);
    String product = "Samsung galaxy s6";

    home.openProductByName(product);

    var productPage = new ProductPage(driver, wait);
    Assertions.assertThat(productPage.productName()).isEqualTo(product);

    productPage.addToCartAcceptAlert();
    productPage.goToCart();

    var cart = new CartPage(driver, wait);
    Assertions.assertThat(cart.hasProduct(product)).isTrue();

    cart.placeOrder("Jane Tester", "RO", "Brasov", "5555444433331111", "12", "2030");
    Assertions.assertThat(cart.confirmationTitle()).containsIgnoringCase("Thank you");
  }
}
