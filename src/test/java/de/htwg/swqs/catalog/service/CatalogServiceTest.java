package de.htwg.swqs.catalog.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.repository.CatalogRepository;
import de.htwg.swqs.catalog.utils.ProductNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

public class CatalogServiceTest {

  // the sample products for the tests
  private List<Product> productList = Arrays.asList(
      new Product(10001L, "A", "aaa", new BigDecimal("1"),100),
      new Product(10002L, "B", "bbb", new BigDecimal("2"),200),
      new Product(10003L, "C", "ccc", new BigDecimal("3"),300),
      new Product(10004L, "D", "ddd", new BigDecimal("4"),400),
      new Product(10005L, "E", "eee", new BigDecimal("5"),500),
      new Product(10006L, "F", "fff", new BigDecimal("6"),600)
  );

  @Test
  public void getAllProductsSuccessful() {
    // setup
    CatalogRepository catalogRepository = mock(CatalogRepository.class);
    CatalogService catalogService = new CatalogServiceImpl(catalogRepository);
    when(catalogRepository.findAll()).thenReturn(productList);

    // execute
    List<Product> retrievedList = catalogService.getAllProducts();

    // verify
    assertEquals(productList, retrievedList);
  }

  @Test(expected = ProductNotFoundException.class)
  public void getAllProductsAndRetrieveEmptyList() {
    // setup
    CatalogRepository catalogRepository = mock(CatalogRepository.class);
    CatalogService catalogService = new CatalogServiceImpl(catalogRepository);
    when(catalogRepository.findAll()).thenReturn(new ArrayList<>());

    // execute
    List<Product> retrievedList = catalogService.getAllProducts();

    // The test is verified by the expected exception
  }

  @Test
  public void getProductByIdSuccessful() {
    // setup
    CatalogRepository catalogRepository = mock(CatalogRepository.class);
    CatalogService catalogService = new CatalogServiceImpl(catalogRepository);
    Product sampleProduct = new Product(42L, "TestProduct", "bla bla", new BigDecimal("54"),100);
    when(catalogRepository.findById(42L)).thenReturn(Optional.of(sampleProduct));

    // execute
    Product retrievedProduct = catalogService.getProductById(42L);

    // verify
    assertEquals(sampleProduct, retrievedProduct);
  }

  @Test(expected = ProductNotFoundException.class)
  public void getProductByIdThrowsException() {
    // setup
    CatalogRepository catalogRepository = mock(CatalogRepository.class);
    CatalogService catalogService = new CatalogServiceImpl(catalogRepository);
    when(catalogRepository.findById(anyLong())).thenReturn(Optional.empty());

    // execute
    Product retrievedProduct = catalogService.getProductById(42L);

    // The test is verified by the expected exception
  }
}