package de.htwg.swqs.order.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Transient;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
public class OrderItem {

  @Id
  @GeneratedValue
  private long id;

  @Positive
  private int quantity;

  private long productId;

  @Column(precision = 19, scale = 4)
  private BigDecimal priceEuro;

  @Transient
  private int weight;

  public OrderItem() {
  }

  public OrderItem(@Positive int quantity, @NotNull long productId, BigDecimal priceEuro, int weight) {
    this.quantity = quantity;
    this.productId = productId;
    this.priceEuro = priceEuro;
    this.weight = weight;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public long getProductId() {
    return productId;
  }

  public void setProductId(long productId) {
    this.productId = productId;
  }

  public BigDecimal getPriceEuro() {
    return priceEuro;
  }

  public void setPriceEuro(BigDecimal priceEuro) {
    this.priceEuro = priceEuro;
  }

  public long getId() {
    return id;
  }

  public int getWeight() {return weight; }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Quantity : ").append(quantity).append("ProductId : ").append(productId).append(" Price : ").append(priceEuro)
            .append(" Weight: ").append(weight);
    return builder.toString();
  }

}
