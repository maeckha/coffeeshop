package de.htwg.swqs.cart.service;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.model.ShoppingCartItem;

public interface CartService {

  ShoppingCart createNewShoppingCart();

  ShoppingCart clearShoppingCart(long cartId);

  ShoppingCart getShoppingCart(long cartId);

  ShoppingCart removeItemFromCart(long cartId, ShoppingCartItem item);

  ShoppingCart removeItemFromCart(long cartId, long productId, int quantity);

  ShoppingCart addItemToCart(long cartId, ShoppingCartItem item);

  ShoppingCart addItemToCart(long cartId, long productId, int quantity);

  ShoppingCart addItemAmountFromShoppingCard(long cartId, long productId, int quantity);

  ShoppingCart substractItemAmountFromShoppingCard(long cartId, long productId, int quantity);

  String shoppingCartsToString();

}
