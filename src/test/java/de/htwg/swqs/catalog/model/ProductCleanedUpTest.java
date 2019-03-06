package de.htwg.swqs.catalog.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Here we demonstrate how to test a pojo.
 *
 * This is a good example to demonstrate usage of @before / @after usage.
 *
 */
public class ProductCleanedUpTest {

  private Product p,p2,o,o2;

  @Before
  public void createSampleProducts() {
    p = new Product(1L, "some name", "some description", new BigDecimal("0.99"),100);
    p2 = new Product(1L, "some name", "some description", new BigDecimal("0.99"),100);
    o = new Product(2L, "some name", "some description", new BigDecimal("0.99"),100);
    o2 = new Product(2L, "another name", "another description", new BigDecimal("0.99"),100);
  }

  @Test
  public void setterAndGetterTest() {
    // setup
    String name = "some product";
    String description = "some description";
    long id = 1L;
    BigDecimal priceEuro = new BigDecimal("3.14");
    Product p = new Product();

    // execute
    p.setId(id);
    p.setName(name);
    p.setDescription(description);
    p.setPriceEuro(priceEuro);

    // verify
    assertEquals(id, p.getId());
    assertEquals(name, p.getName());
    assertEquals(description, p.getDescription());
    assertEquals(priceEuro, p.getPriceEuro());
  }

  @Test
  public void controllerWithParameterTest() {
    // verify
    assertEquals(1L, p.getId());
    assertEquals("some name", p.getName());
    assertEquals("some description", p.getDescription());
    assertEquals(new BigDecimal("0.99"), p.getPriceEuro());
  }

  @Test
  public void toStringTest() {
    // execute
    String returnedString = p.toString();
    // verify
    assertEquals("Product{id=1, name='some name', description='some description', priceEuro=0.99, weight=100}",returnedString);
  }

  @Test
  public void hashCodeTest() {
    // execute
    int hashP = p.hashCode();
    int hashP2 = p2.hashCode();
    int hashO = o.hashCode();
    // verify
    assertNotEquals(hashP, hashO);
    assertEquals(hashP, hashP2);
  }

  @Test
  public void equals() {
    // verify
    assertEquals(p,p);
    // Same properties
    assertEquals(p,p2);
    // Different id
    assertNotEquals(p,o);
    // Different name
    assertNotEquals(o,o2);
  }
}