package de.htwg.swqs.shopui;

import static org.junit.Assert.assertNotNull;

import de.htwg.swqs.shopui.controller.CartController;
import de.htwg.swqs.shopui.controller.CartViewController;
import de.htwg.swqs.shopui.controller.CatalogController;
import de.htwg.swqs.shopui.controller.OrderController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * That's the default test which is delivered with spring boot. It check's the application context
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopUiApplicationTest {

  @Autowired
  private CatalogController catalogController;
  @Autowired
  private CartController cartController;
  @Autowired
  private OrderController orderController;
  @Autowired
  private CartViewController cartViewController;

  @Test
  public void contextLoads() {
    assertNotNull(catalogController);
    assertNotNull(cartController);
    assertNotNull(orderController);
    assertNotNull(cartViewController);
  }
}
