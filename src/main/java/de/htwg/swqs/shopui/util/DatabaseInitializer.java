package de.htwg.swqs.shopui.util;

import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.repository.CatalogRepository;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializer.class);

  CatalogRepository catalogRepository;

  @Autowired
  public DatabaseInitializer(CatalogRepository catalogRepository) {
    this.catalogRepository = catalogRepository;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    if (catalogRepository.count() == 0) {
      catalogRepository
          .save(new Product(10001L, "Name for the product", "Description for the product",
              new BigDecimal("3.14")));
      catalogRepository.save(
          new Product(10002L, "Another Name for another product", "And the description",
              new BigDecimal("2.22")));
      catalogRepository.save(
          new Product(10003L, "Product name", "And the description",
              new BigDecimal("42.99")));
      catalogRepository.flush();
    } else {
      LOGGER
          .debug("Amount of products already available in database: " + catalogRepository.count());
    }


  }
}
