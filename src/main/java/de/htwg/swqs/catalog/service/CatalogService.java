package de.htwg.swqs.catalog.service;

import de.htwg.swqs.catalog.model.Product;
import java.util.List;

public interface CatalogService {

  List<Product> getAllProducts();

  Product getProductById(long id);
}
