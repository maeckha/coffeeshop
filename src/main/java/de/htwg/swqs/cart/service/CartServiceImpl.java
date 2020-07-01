package de.htwg.swqs.cart.service;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.model.ShoppingCartItem;
import de.htwg.swqs.cart.utils.ShoppingCartException;
import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.service.CatalogService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

  private static Map<Long, ShoppingCart> shoppingCarts = new ConcurrentHashMap<>();
  private CatalogService catalogService;

  @Autowired
  public CartServiceImpl(CatalogService catalogService) {
    this.catalogService = catalogService;
  }


  /**
   * Finder method to get a shopping cart identified by a id from the cart list.
   *
   * @param cartId The id of the cart which will be retrieved
   * @return The shopping cart if it exist, otherwise a ShoppingCartException
   */
  public ShoppingCart getShoppingCart(long cartId) {

    if (!shoppingCarts.containsKey(cartId)) {
      throw new ShoppingCartException("Shopping cart does not exist");
    }

    return shoppingCarts.get(cartId);

  }

  /**
   * Removes a given item from the cart if it exist on the cart. Throws a ShoppingCartException if
   * cart does not exist.
   *
   * @param cartId The cart from which the item will be removed
   * @param item The ShoppingCartItem which will be removed
   * @return The updated shopping cart
   */
  public ShoppingCart removeItemFromCart(long cartId, ShoppingCartItem item) {

    if (!shoppingCarts.containsKey(cartId)) {
      throw new ShoppingCartException("Shopping cart does not exist");
    }
    if (item.getQuantity() < 0) {
      throw new ShoppingCartException("Can not remove negative quantity");
    }

    ShoppingCart cart = shoppingCarts.get(cartId);
    // try to get the existing shopping cart item with the same product
    // which we want to remove (or reduce the quantity)
    Optional<ShoppingCartItem> existingShoppingCartItem = getExistingItemFromShoppingCart(
        cart.getItemsInShoppingCart(), item.getProduct());
    if (!existingShoppingCartItem.isPresent()) {
      throw new ShoppingCartException("Shopping cart does not contain the product");
    }

    // quantity of item in shopping cart
    int quantityOfItemBeforeUpdate = existingShoppingCartItem.get().getQuantity();
    // if not enough items in shopping cart throw exception
    if (quantityOfItemBeforeUpdate < item.getQuantity()) {
      throw new ShoppingCartException(
          "Removing " + item.getQuantity() + " items from shopping cart not possible (just "
              + quantityOfItemBeforeUpdate + " present)");
    }

    // update the total sum of the cart
    BigDecimal priceOfTheRemovedItems = BigDecimal.valueOf(item.getQuantity())
        .multiply(item.getProduct().getPriceEuro());
    BigDecimal cartTotalSum = cart.getCartTotalSum().subtract(priceOfTheRemovedItems);
    cart.setCartTotalSum(cartTotalSum);
    checkForTotalDiscount(cartTotalSum, cart);

    // update the quantity of the shopping cart item
    int newQuantityOfProduct = existingShoppingCartItem.get().getQuantity() - item.getQuantity();
    if (newQuantityOfProduct == 0) {
      cart.getItemsInShoppingCart().remove(existingShoppingCartItem.get());
    } else {
      existingShoppingCartItem.get().setQuantity(newQuantityOfProduct);
    }

    return cart;
  }

  /**
   * Removes a given item from the cart if it exist on the cart. Throws a ShoppingCartException if
   * cart does not exist. Same method exists with other signature.
   *
   * @param cartId The cart from which the item will be removed
   * @param productId The product id of the item which will be removed
   * @param quantity The quantity of items we will remove
   * @return The updated ShoppingCart
   */
  public ShoppingCart removeItemFromCart(long cartId, long productId, int quantity) {

    Product product = this.catalogService.getProductById(productId);
    ShoppingCartItem item = new ShoppingCartItem();
    item.setQuantity(quantity);
    item.setProduct(product);

    return removeItemFromCart(cartId, item);
  }

  public ShoppingCart addItemAmountFromShoppingCard(long cartId, long productId, int quantity) {
    ShoppingCart shoppingCart = this.getShoppingCart(cartId);
    List<ShoppingCartItem> items = shoppingCart.getItemsInShoppingCart();
    Product product = this.catalogService.getProductById(productId);

    items.forEach(item -> {
      if(item.getProduct().equals(product)) {
        BigDecimal cartTotalSum = shoppingCart.getCartTotalSum().add(product.getPriceEuro());
        shoppingCart.setCartTotalSum(cartTotalSum);
        checkForTotalDiscount(cartTotalSum, shoppingCart);
        item.setQuantity(item.getQuantity() + 1);
      }
    });
    return shoppingCart;
  }

    public ShoppingCart substractItemAmountFromShoppingCard(long cartId, long productId, int quantity) {
    ShoppingCart shoppingCart = this.getShoppingCart(cartId);
    List<ShoppingCartItem> items = shoppingCart.getItemsInShoppingCart();
    Product product = this.catalogService.getProductById(productId);

    items.forEach(item -> {
      if(item.getProduct().equals(product)) {
        BigDecimal cartTotalSum = shoppingCart.getCartTotalSum().subtract(product.getPriceEuro());
        shoppingCart.setCartTotalSum(cartTotalSum);
        checkForTotalDiscount(cartTotalSum, shoppingCart);
        item.setQuantity(item.getQuantity() - 1);
      }
    });
    return shoppingCart;
  }



  /**
   * Adds the given item to the shopping cart identified by the cartId. Throws exception if cart not
   * exist.
   *
   * @param cartId The id of the cart where the item will be added
   * @param item ShoppingCartItem which will be added
   * @return The updated ShoppingCart
   */
  public ShoppingCart addItemToCart(long cartId, ShoppingCartItem item) {

    if (!shoppingCarts.containsKey(cartId)) {
      throw new ShoppingCartException("Shopping cart does not exist");
    }
    if (item.getQuantity() < 0) {
      throw new ShoppingCartException("Can not add negative quantity");
    }

    ShoppingCart cart = shoppingCarts.get(cartId);
    // try to get the existing shopping cart item with the same product
    // which we want to add (or raise the quantity)
    Optional<ShoppingCartItem> existingShoppingCartItem = getExistingItemFromShoppingCart(
        cart.getItemsInShoppingCart(), item.getProduct());

    if (existingShoppingCartItem.isPresent()) {
      // the product is already in the cart
      int quantityOfItemBeforeUpdate = existingShoppingCartItem.get().getQuantity();
      existingShoppingCartItem.get().setQuantity(quantityOfItemBeforeUpdate + item.getQuantity());
    } else {
      // product not there, add it to cart
      cart.getItemsInShoppingCart().add(item);
    }

    // update the total sum of the cart
    BigDecimal priceOfTheAddedItems = BigDecimal.valueOf(item.getQuantity())
        .multiply(item.getProduct().getPriceEuro());
    BigDecimal cartTotalSum = cart.getCartTotalSum().add(priceOfTheAddedItems);
    cart.setCartTotalSum(cartTotalSum);
    checkForTotalDiscount(cartTotalSum, cart);

    return cart;
  }

  /**
   * Adds the given item to the shopping cart identified by the cartId. Throws exception if cart not
   * exist.
   *
   * @param cartId The id of the cart where the item will be added
   * @param productId The id of the product which will be added
   * @param quantity The quantity of the product
   * @return The updated ShoppingCart
   */
  public ShoppingCart addItemToCart(long cartId, long productId, int quantity) {
    ShoppingCartItem item = new ShoppingCartItem();
    item.setProduct(this.catalogService.getProductById(productId));
    item.setQuantity(quantity);
    return addItemToCart(cartId, item);
  }

  /**
   * Clear the whole shopping cart identified by the given cartId.
   *
   * @param cartId Id of the shopping cart which will be cleared
   * @return The updated ShoppingCart
   */
  public ShoppingCart clearShoppingCart(long cartId) {

    if (!shoppingCarts.containsKey(cartId)) {
      throw new ShoppingCartException("Shopping cart does not exist");
    }

    ShoppingCart cart = shoppingCarts.get(cartId);
    // replace the itemlist with empty arraylist
    cart.setItemsInShoppingCart(new ArrayList<>());
    // reset the cart total sum
    cart.setCartTotalSum(new BigDecimal("0.00"));
    return cart;
  }

  /**
   * Creates a new shopping cart and add it to the central shopping cart list.
   *
   * @return The created shopping cart
   */
  public ShoppingCart createNewShoppingCart() {
    ShoppingCart cart = new ShoppingCart();
    shoppingCarts.put(cart.getId(), cart);
    return cart;
  }

  /**
   * For debug purposes.
   *
   * @return A string which represents the shopping cart list.
   */
  public String shoppingCartsToString() {
    StringBuilder builder = new StringBuilder();
    shoppingCarts.forEach((k, v) -> {
      builder.append("{ id: ");
      builder.append(v.getId());
      builder.append(" | # items: ");
      builder.append(v.getItemsInShoppingCart().size());
      builder.append(" | sum: ");
      builder.append(v.getCartTotalSumDiscount());
      builder.append(" } \n");
    });

    return builder.toString();
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

  //users get a 5% discount, when they're total hits 100
  private void checkForTotalDiscount(BigDecimal cartTotalSum, ShoppingCart cart) {
    if ((cartTotalSum.compareTo(new BigDecimal(100)) != -1)) {
      cart.setDiscount(true);
    } else {
      cart.setDiscount(false);
    }
  }
}
