package de.htwg.swqs.catalog.model;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

@Entity
public class Product {

  @Id
  private long id;
  @NotEmpty
  private String name;
  @NotEmpty
  private String description;
  /**
   * The price of the product (we don't take care of tax).
   */
  @PositiveOrZero
  private BigDecimal priceEuro;

  private int weight;

  public Product() {
  }

  /**
   * Creates a new product entity.
   *
   * @param id  Unique id of the product
   * @param name  Name of the product
   * @param description A description of the product
   * @param priceEuro BigDecimal which represents the cost of the product in euro (without tax)
   */
  public Product(long id, String name, String description, BigDecimal priceEuro, int weight) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.priceEuro = priceEuro;
    this.weight = weight;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPriceEuro() {
    return priceEuro;
  }

  public void setPriceEuro(BigDecimal priceEuro) {
    this.priceEuro = priceEuro;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public int getWeight() {
    return this.weight;
  }

  @Override
  public String toString() {
    return "Product{"
        + "id=" + id
        + ", name='" + name + '\''
        + ", description='" + description + '\''
        + ", priceEuro=" + priceEuro
        + ", weight=" + weight
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Product product = (Product) o;
    return id == product.id
        && Objects.equals(name, product.name)
        && Objects.equals(description, product.description)
        && Objects.equals(priceEuro, product.priceEuro);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, priceEuro);
  }
}
