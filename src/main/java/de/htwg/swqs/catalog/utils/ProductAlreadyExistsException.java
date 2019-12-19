package de.htwg.swqs.catalog.utils;

/**
 * Typed exception for the case no product can be found.
 */
public class ProductAlreadyExistsException extends RuntimeException {

  public ProductAlreadyExistsException(String exception) {
    super(exception);
  }
}
