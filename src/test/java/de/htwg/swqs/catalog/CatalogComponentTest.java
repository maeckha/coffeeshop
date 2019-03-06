package de.htwg.swqs.catalog;

import static org.junit.Assert.assertEquals;

import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.service.CatalogService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This component test uses all classes of the catalog component and tests them together. There's
 * only one test case implemented for demonstration purposes.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatalogConfiguration.class)
@DataJpaTest
public class CatalogComponentTest {

  @Autowired
  CatalogService catalogService;

  @Autowired
  private TestEntityManager entityManager;

  // the sample products for the tests
  private List<Product> productList = Arrays.asList(
          new Product(10001L, "A", "aaa", new BigDecimal("1"),100),
          new Product(10002L, "B", "bbb", new BigDecimal("2"),200),
          new Product(10003L, "C", "ccc", new BigDecimal("3"),300),
          new Product(10004L, "D", "ddd", new BigDecimal("4"),400),
          new Product(10005L, "E", "eee", new BigDecimal("5"),500),
          new Product(10006L, "F", "fff", new BigDecimal("6"),600)
  );

  @Before
  public void setUp() {

    this.productList.forEach(product -> this.entityManager.persist(product));
    this.entityManager.flush();
  }

  @Test
  public void getAllProducts() {

    // setup is done by the @before annotated method

    // execute
    List<Product> retrievedProductList = this.catalogService.getAllProducts();

    // verify
    assertEquals(this.productList, retrievedProductList);
  }

  @Test
  public void getProductById() {

    // execute
    Product retrievedProduct = this.catalogService.getProductById(this.productList.get(0).getId());

    // verify
    assertEquals(this.productList.get(0), retrievedProduct);

  }
}
