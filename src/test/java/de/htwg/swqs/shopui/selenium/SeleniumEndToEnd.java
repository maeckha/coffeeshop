package de.htwg.swqs.shopui.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.service.CartService;
import de.htwg.swqs.catalog.repository.CatalogRepository;
import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.repository.OrderRepository;
import de.htwg.swqs.shopui.selenium.pages.OrderFormPage;
import de.htwg.swqs.shopui.selenium.pages.OrderVerifyPage;
import de.htwg.swqs.shopui.selenium.pages.ProductCatalogPage;
import de.htwg.swqs.shopui.selenium.pages.ProductDetailPage;
import de.htwg.swqs.shopui.selenium.pages.ShoppingCartPage;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * With this test we want to test the whole application with all components included. It is kept
 * simple without a seperate SeleniumConfig and Page Objects.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class SeleniumEndToEnd {

  @LocalServerPort
  private int port;

  private URL base;

  private WebDriver driver;

  @Autowired
  CatalogRepository catalogRepository;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  CartService cartService;

  private SeleniumConfig config;

  private ShoppingCartPage shoppingCartPage;
  private ProductCatalogPage productCatalogPage;
  private ProductDetailPage productDetailPage;
  private OrderFormPage orderFormPage;
  private OrderVerifyPage orderVerifyPage;

  @Before
  public void setUp() throws MalformedURLException {
    this.config = new SeleniumConfig(this.port);
    this.productCatalogPage = new ProductCatalogPage(this.config);
    this.shoppingCartPage = new ShoppingCartPage(this.config);
    this.orderFormPage = new OrderFormPage(this.config);
    this.orderVerifyPage = new OrderVerifyPage(this.config);
  }

  @After
  public void teardown() {
    config.close();
  }

  /**
   * As first step we want to add some products (our cart id is generated when adding the first
   * element).
   */
  @Test
  public void getProductCatalogPage() {
    this.productCatalogPage.navigate();
    assertTrue(this.productCatalogPage.getProductElements().size() > 0);
    assertEquals("not all products from the db are showed", this.catalogRepository.findAll().size(),
        this.productCatalogPage.getProductElements().size());
  }

  @Test
  public void openProductDetailAndAddToCart() {
    long idFromProductWeWantToAdd = this.catalogRepository.findAll().get(0).getId();
    int amountWeWantToAdd = 2;

    this.productDetailPage = new ProductDetailPage(this.config, idFromProductWeWantToAdd);
    this.productDetailPage.navigate();
    this.productDetailPage.addItemToCart(amountWeWantToAdd);

    // verify cookie and corresponding cart exists
    Cookie cookie = this.config.getDriver().manage().getCookieNamed("cart-id");
    ShoppingCart cart = this.cartService.getShoppingCart(Long.parseLong(cookie.getValue()));
    assertNotNull(cart);

    System.out.println("Id we want to add = " + idFromProductWeWantToAdd);
    System.out.println(cartService.shoppingCartsToString());

    // verify correct product is added to cart
    assertEquals(idFromProductWeWantToAdd,
        cart.getItemsInShoppingCart().get(0).getProduct().getId());
    // in correct quantity
    assertEquals(amountWeWantToAdd,
        cart.getItemsInShoppingCart().get(0).getQuantity());
  }

  @Test
  public void openShoppingCartOverviewAndTriggerOrder() {
    //setup -> add items to cart
    long idFromProductWeWantToAdd = this.catalogRepository.findAll().get(0).getId();
    int amountWeWantToAdd = 2;
    this.productDetailPage = new ProductDetailPage(this.config, idFromProductWeWantToAdd);
    this.productDetailPage.navigate();
    this.productDetailPage.addItemToCart(amountWeWantToAdd);

    // execute
    this.shoppingCartPage.navigate();

    // verify
    this.shoppingCartPage.clickOrderButton();
    assertEquals("http://localhost:" + this.port + "/order",
        this.config.getDriver().getCurrentUrl());
  }

  @Test
  public void fillInOrderForm() {
    //setup
    long idFromProductWeWantToAdd = this.catalogRepository.findAll().get(0).getId();
    int amountWeWantToAdd = 2;
    this.productDetailPage = new ProductDetailPage(this.config, idFromProductWeWantToAdd);
    this.productDetailPage.navigate();
    this.productDetailPage.addItemToCart(amountWeWantToAdd);

    this.shoppingCartPage.navigate();
    this.shoppingCartPage.clickOrderButton();

    // execute
    this.orderFormPage.fillInFormWithDummyData();
    this.orderFormPage.submitForm();
  }

  @Test
  public void checkAllInfoAreShowedAndSubmitOrder() {
    //setup
    long idFromProductWeWantToAdd = this.catalogRepository.findAll().get(0).getId();
    int amountWeWantToAdd = 2;
    this.productDetailPage = new ProductDetailPage(this.config, idFromProductWeWantToAdd);
    this.productDetailPage.navigate();
    this.productDetailPage.addItemToCart(amountWeWantToAdd);

    this.shoppingCartPage.navigate();
    this.shoppingCartPage.clickOrderButton();

    this.orderFormPage.fillInFormWithDummyData();
    this.orderFormPage.submitForm();

    this.orderVerifyPage.submitForm();

    // execute
    Order order = this.orderRepository.findAll().get(0);
    assertNotNull(order);
    System.out.println(order.toString());

  }


}
