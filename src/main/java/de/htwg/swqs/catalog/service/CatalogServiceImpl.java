package de.htwg.swqs.catalog.service;

import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.repository.CatalogRepository;
import de.htwg.swqs.catalog.utils.ProductAlreadyExistsException;
import de.htwg.swqs.catalog.utils.ProductNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogServiceImpl implements CatalogService {

  private CatalogRepository catalogRepository;

  @Autowired
  public CatalogServiceImpl(CatalogRepository catalogRepository) {
    this.catalogRepository = catalogRepository;
  }

  @Override
  public List<Product> getAllProducts() {
    List<Product> productList = this.catalogRepository.findAll();

    if (productList.isEmpty()) {
      throw new ProductNotFoundException("No products available");
    }
    return productList;
  }

  @Override
  public Product getProductById(long id) {
    Optional<Product> product = this.catalogRepository.findById(id);

    if (!product.isPresent()) {
      throw new ProductNotFoundException("Product with id " + id + " not found");
    }
    return product.get();
  }

  @Override
  public void createProduct(Product product){

    long id = product.getId();
    Optional<Product> productTest = this.catalogRepository.findById(id);

    if (productTest.isPresent()) {
      System.out.println("Exception");
      throw new ProductAlreadyExistsException("Product with id " + id + " already exists");
    }

    this.catalogRepository.save(product);
  }
}
