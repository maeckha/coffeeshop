package de.htwg.swqs.catalog;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("de.htwg.swqs.catalog")
@EnableJpaRepositories("de.htwg.swqs.catalog.repository")
@EntityScan("de.htwg.swqs.catalog.model")
public class CatalogConfiguration {

  // the beans are already defined by the class annotations
}
