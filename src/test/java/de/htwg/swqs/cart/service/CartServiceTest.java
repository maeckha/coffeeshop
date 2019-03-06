package de.htwg.swqs.cart.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.model.ShoppingCartItem;
import de.htwg.swqs.cart.utils.ShoppingCartException;
import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.service.CatalogService;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * The DOC catalog service is never used in this tests, so we must not configure of the mock. In
 * this special case we call the test double an dummy object.
 */
public class CartServiceTest {

  @Mock
  private CatalogService catalogService;

  private CartService cartService;

  @Before
  public void setUp() {
    this.cartService = new CartServiceImpl(this.catalogService);
  }

  @Test
  public void createNewShoppingCart() {

    // execute
    ShoppingCart newCart = this.cartService.createNewShoppingCart();

    //verify
    assertNotNull(newCart);
    // assert that the id is set correctly
    assertTrue("id of newly created shopping cart", newCart.getId() > 0L);
    // and the newly created cart got no items
    assertTrue(newCart.getItemsInShoppingCart().isEmpty());
  }


  @Test
  public void getShoppingCartThatExists() {
    // setup
    ShoppingCart createdCart = this.cartService.createNewShoppingCart();

    // execute
    ShoppingCart retrievedCart = this.cartService.getShoppingCart(createdCart.getId());

    // verify
    assertEquals(createdCart, retrievedCart);
  }


  @Test(expected = ShoppingCartException.class)
  public void getShoppingCartThatDoesNotExist() {
    // setup
    ShoppingCart cart = this.cartService.createNewShoppingCart();

    // execute
    this.cartService.getShoppingCart(9999999);

    // The test is verified by the expected exception
  }

  @Test
  public void clearShoppingCart() {
    // setup
    CatalogService catalogServiceMock = mock(CatalogService.class);
    CartService cartService = new CartServiceImpl(catalogServiceMock);
    ShoppingCart cart = cartService.createNewShoppingCart();
    Product prod = new Product(1234, "Sample product", "a description", new BigDecimal("0.99"),1000);
    ShoppingCartItem item = new ShoppingCartItem(1, prod);
    cartService.addItemToCart(cart.getId(), item);

    // execute
    cartService.clearShoppingCart(cart.getId());

    // verify
    assertTrue(cart.getItemsInShoppingCart().isEmpty());
    assertEquals(new BigDecimal("0.00"), cart.getCartTotalSum());
  }

  @Test(expected = ShoppingCartException.class)
  public void clearShoppingCartThatDoesNotExist() {
    // setup
    CatalogService catalogServiceMock = mock(CatalogService.class);
    CartService cartService = new CartServiceImpl(catalogServiceMock);
    ShoppingCart cart = cartService.createNewShoppingCart();
    Product prod = new Product(1234, "Sample product", "a description", new BigDecimal("0.99"),1000);
    ShoppingCartItem item = new ShoppingCartItem(1, prod);
    cartService.addItemToCart(cart.getId(), item);

    // execute
    cartService.clearShoppingCart(9999999);

    // The test is verified by the expected exception
  }
}
