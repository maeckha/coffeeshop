package de.htwg.swqs.catalog.repository;

import de.htwg.swqs.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * https://projects.spring.io/spring-data-jpa/
 *
 * <p> The repository interface to access the product database; with spring data we need only the
 * interface, all the default crud operations are deleted by default. </p>
 *
 * <p> Only custom queries has to be declared! </p>
 */
public interface CatalogRepository extends JpaRepository<Product, Long> {

  // findAll & findById is implicit implemented by spring data

}



