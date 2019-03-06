package de.htwg.swqs.cart;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.model.ShoppingCartItem;
import de.htwg.swqs.cart.service.CartService;
import de.htwg.swqs.cart.service.CartServiceImpl;
import de.htwg.swqs.catalog.CatalogConfiguration;
import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.repository.CatalogRepository;
import de.htwg.swqs.catalog.service.CatalogService;
import de.htwg.swqs.catalog.service.CatalogServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Here we will test the connection to the catalog component, we will use the real catalog
 * repository with an h2 test database, delivered by the @DataJpaTest Annotation.
 *
 * We focus in this test on the connection between the both components. The verification of the
 * interaction is done with a Mockito Spy-Object.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CartConfiguration.class, CatalogConfiguration.class})
@DataJpaTest
public class CartCatalogIT {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private CatalogRepository catalogRepository;

  @Test
  public void getProductFromCatalogServiceWhenRemovingItemFromCart() {
    // setup
    CatalogService catalogService = new CatalogServiceImpl(this.catalogRepository);
    CatalogService catalogServiceSpy = spy(catalogService);
    CartService cartService = new CartServiceImpl(catalogServiceSpy);
    Product sampleProduct = new Product(1, "Sample Product", "just a sample product",
        BigDecimal.valueOf(3.14),1000);
    entityManager.persist(sampleProduct);
    entityManager.flush();

    ShoppingCart cart = cartService.createNewShoppingCart();
    ShoppingCartItem item = new ShoppingCartItem(3, sampleProduct);
    cartService.addItemToCart(cart.getId(), sampleProduct.getId(), 3);

    // execute
    cartService.removeItemFromCart(cart.getId(), sampleProduct.getId(), 3);

    // verify, that the interaction between the components works
    verify(catalogServiceSpy, times(2)).getProductById(anyLong());
    assertTrue(cartService.getShoppingCart(cart.getId()).getItemsInShoppingCart().isEmpty());

  }

  @Test
  public void getProductFromCatalogServiceWhenAddingItemToCart() {
    // setup
    CatalogService catalogService = new CatalogServiceImpl(this.catalogRepository);
    CatalogService catalogServiceSpy = spy(catalogService);
    CartService cartService = new CartServiceImpl(catalogServiceSpy);
    Product sampleProduct = new Product(1, "Sample Product", "just a sample product",
        BigDecimal.valueOf(3.14),1000);
    entityManager.persist(sampleProduct);
    entityManager.flush();

    ShoppingCart cart = cartService.createNewShoppingCart();

    // excecute
    cartService.addItemToCart(cart.getId(), sampleProduct.getId(), 3);

    // verify
    verify(catalogServiceSpy, times(1)).getProductById(anyLong());

  }
}
