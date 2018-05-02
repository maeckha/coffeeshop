package de.htwg.swqs.shopui.controller;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.service.CartService;
import de.htwg.swqs.shopui.util.ItemRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("carts/")
public class CartController {

  private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

  private CartService cartService;

  @Autowired
  public CartController(CartService cartService) {
    this.cartService = cartService;

  }

  @GetMapping(value = "create")
  public long createNewCartAndReturnId() {

    ShoppingCart newCart = this.cartService.createNewShoppingCart();

    LOGGER.debug("Available ShoppingCarts:");
    LOGGER.debug(this.cartService.shoppingCartsToString());

    return newCart.getId();
  }

  @GetMapping(value = "get", produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody
  ShoppingCart getCartById(@CookieValue("cart-id") long cartId) {
    // return the full shopping cart with sum and all products
    return this.cartService.getShoppingCart(cartId);
  }

  @GetMapping(value = "clear")
  public @ResponseBody
  ShoppingCart removeCartById(@CookieValue("cart-id") long cartId) {

    return this.cartService.clearShoppingCart(cartId);
  }

  @PostMapping(
      value = "add",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public @ResponseBody
  ShoppingCart addItemToCart(
      @CookieValue("cart-id") long cartId,
      @RequestBody ItemRequestWrapper itemRequestWrapper) {

    return this.cartService.addItemToCart(cartId, itemRequestWrapper.getProductId(),
        itemRequestWrapper.getQuantity());
  }

  @PostMapping(
      value = "/remove",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public @ResponseBody
  ShoppingCart removeItemFromCart(
      @CookieValue("cart-id") long cartId,
      @RequestBody ItemRequestWrapper itemRequestWrapper) {

    return this.cartService.removeItemFromCart(cartId, itemRequestWrapper.getProductId(),
        itemRequestWrapper.getQuantity());
  }

}
