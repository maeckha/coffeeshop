package de.htwg.swqs.cart.model;

import de.htwg.swqs.catalog.model.Product;

/**
 * Class will be used to convert the payload of requests to add new products to a java object (with
 * Jackson Json-processor https://github.com/FasterXML/jackson)
 */
public class ShoppingCartItem {


  private int quantity;
  private Product product;

  public ShoppingCartItem() {
  }

  public ShoppingCartItem(int quantity, Product product) {
    this.quantity = quantity;
    this.product = product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Quantity: ").append(quantity).append(" Product: "+product);
    return builder.toString();
  }
}
