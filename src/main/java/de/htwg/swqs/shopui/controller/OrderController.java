package de.htwg.swqs.shopui.controller;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.service.CartService;
import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.model.OrderItem;
import de.htwg.swqs.order.service.OrderService;
import de.htwg.swqs.shopui.util.OrderWrapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {


  private OrderService orderService;
  private CartService cartService;

  @Autowired
  public OrderController(OrderService orderService, CartService cartService) {
    this.orderService = orderService;
    this.cartService = cartService;
  }

  /**
   * Returns the page on which the user can create a order and submit it.
   *
   * @param cartId The id from the shopping cart corresponding to the customer
   * @param model A holder for model attributes
   * @return A String with the name of the template which will be returned
   */
  @GetMapping
  public String showOrderPage(@CookieValue("cart-id") long cartId, Model model) {
    model.addAttribute("title", "E-Commerce Shop | Order");

    ShoppingCart cart = this.cartService.getShoppingCart(cartId);
    model.addAttribute("cart", cart);

    return "create-order";
  }

  /**
   * Creates a new order with the data submitted by the user.
   *
   * @param orderWrapper A wrapper object which contains the customer info and the chosen currency
   * @return The created order object
   */
  @PostMapping
  public @ResponseBody Order createOrder(
      @CookieValue("cart-id") long cartId,
      @RequestBody OrderWrapper orderWrapper
  ) {
    ShoppingCart cart = this.cartService.getShoppingCart(cartId);
    return this.orderService
        .createOrder(orderWrapper.getCustomerInfo(), createOrderItemListFromShoppingCart(cart),
            orderWrapper.getCurrency());
  }


  private List<OrderItem> createOrderItemListFromShoppingCart(ShoppingCart cart) {

    List<OrderItem> orderItems = new ArrayList<>();
    cart.getItemsInShoppingCart().forEach(
        item -> orderItems.add(new OrderItem(item.getQuantity(), item.getProduct().getId(), item.getProduct().getPriceEuro())));
    return orderItems;
  }
}
