package de.htwg.swqs.catalog.utils;

/**
 * Typed exception for the case no product can be found.
 */
public class ProductNotFoundException extends RuntimeException {

  public ProductNotFoundException(String exception) {
    super(exception);
  }
}
