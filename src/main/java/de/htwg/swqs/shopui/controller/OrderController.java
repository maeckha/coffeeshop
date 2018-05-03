package de.htwg.swqs.shopui.controller;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.service.CartService;
import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.model.OrderItem;
import de.htwg.swqs.order.payment.PaymentMethod;
import de.htwg.swqs.order.payment.PaymentMethodService;
import de.htwg.swqs.order.service.OrderService;
import de.htwg.swqs.shopui.util.OrderWrapper;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class OrderController {


  private OrderService orderService;
  private CartService cartService;
  private PaymentMethodService paymentMethodService;

  @Autowired
  public OrderController(OrderService orderService, CartService cartService,
      PaymentMethodService paymentMethodService) {
    this.orderService = orderService;
    this.cartService = cartService;
    this.paymentMethodService = paymentMethodService;
  }

  /**
   * Returns the page on which the user can create a order and submit it.
   *
   * @param cartId The id from the shopping cart corresponding to the customer
   * @param model A holder for model attributes
   * @return A String with the name of the template which will be returned
   */
  @GetMapping("/order")
  public String showOrderPage(@CookieValue("cart-id") long cartId, Model model) {
    model.addAttribute("title", "E-Commerce Shop | Order");

    ShoppingCart cart = this.cartService.getShoppingCart(cartId);
    model.addAttribute("cart", cart);
    model.addAttribute("orderwrapper", new OrderWrapper());

    return "order-create";
  }

  /**
   * Creates a new order with the data submitted by the user and shows them to the user for
   * validation.
   *
   * @param orderWrapper A wrapper object which contains the customer info and the chosen currency
   * @return The created order object
   */
  @PostMapping("/order")
  public String createOrderForVerification(
      @CookieValue("cart-id") long cartId,
      @Valid OrderWrapper orderWrapper,
      Model model
  ) {
    ShoppingCart cart = this.cartService.getShoppingCart(cartId);
    Order createdOrder = this.orderService.createOrder(
        orderWrapper.getCustomerInfo(),
        createOrderItemListFromShoppingCart(cart),
        orderWrapper.getCurrency()
    );
    List<PaymentMethod> paymentMethods = this.paymentMethodService
        .getAcceptedMethods(createdOrder.getCustomerInfo(), createdOrder.getCostTotal());
    if (paymentMethods.isEmpty()) {
      throw new RuntimeException("You are blacklisted, orders are not possible!");
    }

    model.addAttribute("title", "E-Commerce Shop | Validate Order");
    model.addAttribute("order", createdOrder);
    model.addAttribute("paymentMethods", paymentMethods);

    return "order-validate";
  }

  /**
   * Submit the order and persist it.
   *
   * @param order A wrapper object which contains the customer info and the chosen currency
   * @return The created order object
   */
  @PostMapping("/submit-order")
  public String submitOrder(
      @CookieValue("cart-id") long cartId,
      Model model,
      Order order
  ) {
    ShoppingCart cart = this.cartService.getShoppingCart(cartId);
    Order createdOrder = this.orderService.persistOrder(order);

    model.addAttribute("title", "E-Commerce Shop | Order done");
    model.addAttribute("mailAddress", createdOrder.getCustomerInfo().getEmail());

    return "order-done";
  }


  private List<OrderItem> createOrderItemListFromShoppingCart(ShoppingCart cart) {

    List<OrderItem> orderItems = new ArrayList<>();
    cart.getItemsInShoppingCart().forEach(
        item -> orderItems.add(new OrderItem(item.getQuantity(), item.getProduct().getId(),
            item.getProduct().getPriceEuro())));
    return orderItems;
  }
}
