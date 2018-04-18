package de.htwg.swqs.shopui;

import de.htwg.swqs.cart.CartConfiguration;
import de.htwg.swqs.catalog.CatalogConfiguration;
import de.htwg.swqs.order.OrderConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({CatalogConfiguration.class, CartConfiguration.class, OrderConfiguration.class})
public class ShopUiApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShopUiApplication.class, args);
  }
}
