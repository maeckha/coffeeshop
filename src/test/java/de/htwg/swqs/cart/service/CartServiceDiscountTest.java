package de.htwg.swqs.cart.service;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.model.ShoppingCartItem;
import de.htwg.swqs.cart.utils.ShoppingCartException;
import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.service.CatalogService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


public class CartServiceDiscountTest {

  private CartService cartService;
  private ShoppingCart cart;
  private Product prod;

  @Before
  public void setupTestFixture() {
    CatalogService catalogServiceMock = mock(CatalogService.class);
    this.cartService = new CartServiceImpl(catalogServiceMock);
    this.cart = cartService.createNewShoppingCart();
    this.prod = new Product(1234, "Sample product", "a description", BigDecimal.valueOf(10),1000);
  }

  @Test
  public void getDiscountAbove100() {
    // setup
    ShoppingCartItem item = new ShoppingCartItem(10, prod);

    // execute
    cartService.addItemToCart(cart.getId(), item);

    // verify
    assertEquals(new BigDecimal(95.00).setScale(2, RoundingMode.HALF_UP), cartService.getShoppingCart(cart.getId()).getCartTotalSumDiscount());
  }

  @Test
  public void looseDiscountGoingUnder100() {
    // setup
    ShoppingCartItem item = new ShoppingCartItem(10, prod);
    ShoppingCartItem itemToBeRemoved = new ShoppingCartItem(1, prod);

    // execute
    cartService.addItemToCart(cart.getId(), item);
    cartService.removeItemFromCart(cart.getId(), itemToBeRemoved);

    // verify
    assertEquals(new BigDecimal(90), cartService.getShoppingCart(cart.getId()).getCartTotalSumDiscount());
  }

}
