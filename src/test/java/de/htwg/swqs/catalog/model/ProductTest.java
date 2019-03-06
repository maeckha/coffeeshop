package de.htwg.swqs.catalog.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import org.junit.Test;

/**
 * Here we demonstrate how to test a pojo.
 *
 * This is a good example to demonstrate usage of @before / @after usage.
 *
 */
public class ProductTest {

  @Test
  public void setterAndGetterTest() {
    // setup
    String name = "some product";
    String description = "some description";
    long id = 1L;
    BigDecimal priceEuro = new BigDecimal("3.14");
    int weight = 1000;
    Product p = new Product();

    // execute
    p.setId(id);
    p.setName(name);
    p.setDescription(description);
    p.setPriceEuro(priceEuro);
    p.setWeight(weight);

    long retrievedId = p.getId();
    String retrievedName = p.getName();
    String retrievedDescription = p.getDescription();
    BigDecimal retrievedPriceEuro = p.getPriceEuro();

    // verify
    assertEquals(id, retrievedId);
    assertEquals(name, retrievedName);
    assertEquals(description, retrievedDescription);
    assertEquals(priceEuro, retrievedPriceEuro);
    assertEquals(weight, p.getWeight());
  }

  @Test
  public void controllerWithParameterTest() {

    // setup
    String name = "some product";
    String description = "some description";
    long id = 1L;
    BigDecimal priceEuro = new BigDecimal("3.14");
    int weight = 1000;


    // execute
    Product p = new Product(id, name, description, priceEuro, weight);

    // verify
    assertEquals(id, p.getId());
    assertEquals(name, p.getName());
    assertEquals(description, p.getDescription());
    assertEquals(priceEuro, p.getPriceEuro());
    assertEquals(weight, p.getWeight());
  }

  @Test
  public void toStringTest() {
    // setup
    String name = "some product";
    String description = "some description";
    long id = 1L;
    BigDecimal priceEuro = new BigDecimal("3.14");
    int weight = 1000;

    Product p = new Product(id, name, description, priceEuro, weight);

    // execute
    String returnedString = p.toString();

    // verify
    assertEquals("Product{id=1, name='some product', description='some description', priceEuro=3.14, weight=1000}",returnedString);
  }

  @Test
  public void hashCodeTest() {
    // setup
    Product p = new Product(1L, "some name", "some description", new BigDecimal("0.99"),1000);
    Product o = new Product(2L, "some name", "some description", new BigDecimal("0.99"),1000);

    // execute
    int hashP = p.hashCode();
    int hashO = o.hashCode();

    // verify
    assertNotEquals(hashP, hashO);
  }

  @Test
  public void equalsTestWithSameObject() {
    // setup
    Product p = new Product(1L, "some name", "some description", new BigDecimal("0.99"),1000);

    // verify
    assertTrue(p.equals(p));
  }

  @Test
  public void equalsTestWithSameProperties() {
    // setup
    Product p = new Product(1L, "some name", "some description", new BigDecimal("0.99"),1000);
    Product o = new Product(1L, "some name", "some description", new BigDecimal("0.99"),1000);

    // verify
    assertTrue(p.equals(o));
  }

  @Test
  public void equalsTestWithDifferentObject() {
    // setup
    Product p = new Product(1L, "some name", "some description", new BigDecimal("0.99"),1000);
    Product o = new Product(2L, "another name", "another description", new BigDecimal("0.99"),1000);

    // verify
    assertFalse(p.equals(o));
  }

  @Test
  public void equalsTestWithNull() {
    // setup
    Product p = new Product(1L, "some name", "some description", new BigDecimal("0.99"),1000);

    // verify
    assertFalse(p.equals(null));
  }
}
