package de.htwg.swqs.shopui.util;

import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.repository.CatalogRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class InsertDataAtStartup implements ApplicationListener<ApplicationReadyEvent>{

  @Autowired
  CatalogRepository catalogRepository;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    if (catalogRepository.count() > 0) {
      System.out.println(catalogRepository.count());
      return;
    }

    catalogRepository.save(new Product(1L, "Name for the product", "Description for the product", new BigDecimal("3.14")));
    catalogRepository.save(new Product(2L, "Another Name for another product", "And the description", new BigDecimal("2.22")));
  }
}
