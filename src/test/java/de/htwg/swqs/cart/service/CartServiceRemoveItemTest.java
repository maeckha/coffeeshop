package de.htwg.swqs.cart.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.model.ShoppingCartItem;
import de.htwg.swqs.cart.utils.ShoppingCartException;
import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.service.CatalogService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;


public class CartServiceRemoveItemTest {

  private CartService cartService;
  private ShoppingCart cart;
  private Product prod;

  @Before
  public void setupTestFixture() {
    CatalogService catalogServiceMock = mock(CatalogService.class);
    this.cartService = new CartServiceImpl(catalogServiceMock);
    this.cart = cartService.createNewShoppingCart();
    this.prod = new Product(1234, "Sample product", "a description", BigDecimal.valueOf(0.99),1000);
  }

  @Test
  public void removeItemSuccessfulFromCart() {
    // setup
    int quantityToRemove = 3;
    ShoppingCartItem item = new ShoppingCartItem(quantityToRemove, prod);
    // first add all the items to the cart
    cartService.addItemToCart(cart.getId(), item);
    // create an identical ShoppingCartItem
    ShoppingCartItem anotherItem = new ShoppingCartItem(3, prod);

    // execute
    cartService.removeItemFromCart(cart.getId(), item);

    // verify
    assertFalse(cart.getItemsInShoppingCart().contains(item));
  }

  @Test
  public void removeSomeItemsSuccessfulFromCart() {
    // setup
    int quantityToAdd = 3;
    int quantityToRemove = 2;
    ShoppingCartItem itemToAdd = new ShoppingCartItem(quantityToAdd, prod);
    ShoppingCartItem itemToRemove = new ShoppingCartItem(quantityToRemove, prod);
    // first add all the items to the cart
    cartService.addItemToCart(cart.getId(), itemToAdd);

    // execute
    cartService.removeItemFromCart(cart.getId(), itemToRemove);

    // verify
    assertEquals((quantityToAdd - quantityToRemove), itemToAdd.getQuantity());
    assertTrue(cart.getItemsInShoppingCart().contains(itemToAdd));
  }

  @Test
  public void removeItemFromCartWithQuantityZero() {
    // setup
    int quantityToAdd = 3;
    int quantityToRemove = 0;
    ShoppingCartItem itemToAdd = new ShoppingCartItem(quantityToAdd, prod);
    ShoppingCartItem itemToRemove = new ShoppingCartItem(quantityToRemove, prod);

    // first add all the items to the cart
    cartService.addItemToCart(cart.getId(), itemToAdd);

    // execute
    cartService.removeItemFromCart(cart.getId(), itemToRemove);

    // verify
    assertEquals((quantityToAdd - quantityToRemove), itemToAdd.getQuantity());
  }

  @Test(expected = ShoppingCartException.class)
  public void removeItemFromCartWithNegativeQuantity() {
    // setup
    int quantityToAdd = 3;
    int quantityToRemove = -5;
    ShoppingCartItem itemToAdd = new ShoppingCartItem(quantityToAdd, prod);
    ShoppingCartItem itemToRemove = new ShoppingCartItem(quantityToRemove, prod);

    // first add all the items to the cart
    cartService.addItemToCart(cart.getId(), itemToAdd);

    // execute
    cartService.removeItemFromCart(cart.getId(), itemToRemove);

    // verification is done with the exception

  }

  @Test(expected = ShoppingCartException.class)
  public void removeItemFromCartWhoDoesNotExist() {
    // setup
    int quantityToRemove = 3;
    ShoppingCartItem item = new ShoppingCartItem(quantityToRemove, prod);

    // first add all the items to the cart
    cartService.addItemToCart(cart.getId(), item);

    // execute
    cartService.removeItemFromCart(9999999, item);

    // verification is done with the exception

  }

  @Test(expected = ShoppingCartException.class)
  public void removeItemWhoDoesNotExistFromCart() {
    // setup
    int quantityToRemove = 3;
    ShoppingCartItem item = new ShoppingCartItem(quantityToRemove, prod);
    // first add all the items to the cart
    cartService.addItemToCart(cart.getId(), item);
    // create another ShoppingCartItem with a different product
    Product anotherProd = new Product(2, "Another sample product", "the description",
        BigDecimal.valueOf(42),1000);
    ShoppingCartItem anotherItem = new ShoppingCartItem(2, anotherProd);

    // execute
    cartService.removeItemFromCart(cart.getId(), anotherItem);

    // The test is verified by the expected exception
  }

  @Test(expected = ShoppingCartException.class)
  public void removeItemFromCartInWrongQuantity() {

    // setup
    int quantityToAdd = 3;
    int quantityToRemove = 5;
    ShoppingCartItem itemToAdd = new ShoppingCartItem(quantityToAdd, prod);
    ShoppingCartItem itemToRemove = new ShoppingCartItem(quantityToRemove, prod);

    // first add all the items to the cart
    cartService.addItemToCart(cart.getId(), itemToAdd);

    // execute
    cartService.removeItemFromCart(cart.getId(), itemToRemove);

    // verify
    assertEquals((quantityToAdd - quantityToRemove),
        Collections.frequency(cart.getItemsInShoppingCart(), prod));
    assertTrue(cart.getItemsInShoppingCart().isEmpty());
  }

  private Optional<ShoppingCartItem> getExistingItemFromShoppingCart(
      List<ShoppingCartItem> shoppingCartItems, Product productToRemove) {
    for (ShoppingCartItem tmpItem : shoppingCartItems) {
      if (tmpItem.getProduct().equals(productToRemove)) {
        return Optional.of(tmpItem);
      }
    }
    return Optional.empty();
  }
}
