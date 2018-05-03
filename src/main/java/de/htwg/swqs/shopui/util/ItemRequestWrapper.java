package de.htwg.swqs.shopui.util;

import javax.validation.constraints.NotNull;

public class ItemRequestWrapper {

  @NotNull
  private long productId;
  @NotNull
  private int quantity;

  public ItemRequestWrapper() {
  }

  public long getProductId() {
    return productId;
  }

  public void setProductId(long productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
