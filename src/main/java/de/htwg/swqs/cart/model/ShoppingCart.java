package de.htwg.swqs.cart.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ShoppingCart {

  /**
   * Counter for the shopping cart {@code id}, used when creating new shopping carts.
   */
  private static AtomicLong idGenerator = new AtomicLong();

  // id
  private long id;
  private List<ShoppingCartItem> itemsInShoppingCart;
  private BigDecimal cartTotalSum;
  private boolean discount;

  /**
   * Default constructor which initializes the list for the items and the big decimal for the total
   * sum.
   */
  public ShoppingCart() {
    this.itemsInShoppingCart = new ArrayList<>();
    this.cartTotalSum = BigDecimal.ZERO;
    // set a new id to the created shopping cart
    this.id = idGenerator.incrementAndGet();
    this.discount = false;
  }

  public long getId() {
    return id;
  }

  public List<ShoppingCartItem> getItemsInShoppingCart() {
    return itemsInShoppingCart;
  }

  public void setItemsInShoppingCart(List<ShoppingCartItem> itemsInShoppingCart) {
    this.itemsInShoppingCart = itemsInShoppingCart;
  }

  public BigDecimal getCartTotalSum() {
    return cartTotalSum;
  }

  public BigDecimal getCartTotalSumDiscount() {
    if (discount) {
      return cartTotalSum.multiply(new BigDecimal(0.95)).setScale(2, RoundingMode.HALF_UP);
    }
    return cartTotalSum;
  }

  public void setCartTotalSum(BigDecimal cartTotalSum) {
    this.cartTotalSum = cartTotalSum;
  }

  public boolean isDiscount() {
    return discount;
  }

  public void setDiscount(boolean discount) {
    this.discount = discount;
  }

  @Override
  public String toString() {
    return "ShoppingCart{"
        + "id=" + id
        + ", itemsInShoppingCart=" + itemsInShoppingCart
        + ", cartTotalSum=" + cartTotalSum
        + '}';
  }
}
