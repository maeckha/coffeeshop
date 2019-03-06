package de.htwg.swqs.order;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"de.htwg.swqs.order"})
@EnableJpaRepositories("de.htwg.swqs.order.repository")
@EntityScan("de.htwg.swqs.order.model")
public class OrderConfiguration {

  // the beans are already defined by the class annotations

}
