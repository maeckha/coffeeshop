package de.htwg.swqs.cart;

import de.htwg.swqs.catalog.CatalogConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "de.htwg.swqs.cart")
@EntityScan("de.htwg.swqs.cart.model")
@Import(CatalogConfiguration.class)

public class CartConfiguration {

  // the beans are already defined by the class annotations
}


