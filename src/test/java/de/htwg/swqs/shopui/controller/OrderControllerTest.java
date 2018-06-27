package de.htwg.swqs.shopui.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import de.htwg.swqs.cart.service.CartService;
import de.htwg.swqs.order.payment.PaymentMethodService;
import de.htwg.swqs.order.service.OrderService;
import de.htwg.swqs.shopui.HelperUtil;
import javax.servlet.http.Cookie;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class OrderControllerTest {

  private MockMvc mvc;
  private OrderController orderController;
  private OrderService orderService;
  private CartService cartService;
  private PaymentMethodService paymentMethodService;

  @Before
  public void setUp() {
    this.orderService = mock(OrderService.class);
    this.cartService = mock(CartService.class);
    this.paymentMethodService = mock(PaymentMethodService.class);
    this.orderController = new OrderController(this.orderService, this.cartService,
        this.paymentMethodService);
    this.mvc = MockMvcBuilders.standaloneSetup(this.orderController).build();
  }

  @Test
  public void getOrderFormular() throws Exception {

    // setup
    when(this.cartService.getShoppingCart(1)).thenReturn(HelperUtil.createDummyShoppingCart());

    // execute & verify
    this.mvc.perform(get("/order")
        .accept(MediaType.TEXT_HTML)
        .cookie(new Cookie("cart-id", "1")))
        .andExpect(model()
            .attributeExists("title", "cart", "orderwrapper"))
        .andExpect(view().name("order-create"))
        .andExpect(status().isOk())
        .andDo(print());
  }
}
