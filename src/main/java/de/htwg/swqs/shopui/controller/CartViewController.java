package de.htwg.swqs.shopui.controller;

import de.htwg.swqs.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartViewController {

  private CartService cartService;

  @Autowired
  public CartViewController(CartService cartService) {
    this.cartService = cartService;
  }

  @GetMapping(value = {"/show-cart"})
  public String getShoppingCart(@CookieValue("cart-id") long cartId, Model model) {
    model.addAttribute("title", "E-Commerce Shop | Shopping cart overview");
    model.addAttribute("shoppingCart", this.cartService.getShoppingCart(cartId));
    return "shoppingcart";
  }

}
