package de.htwg.swqs.shopui.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.service.CartService;
import de.htwg.swqs.shopui.HelperUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CartControllerTest {

  private MockMvc mvc;
  private CartService cartService;
  private CartController cartController;


  @Before
  public void setUp() {
    this.cartService = mock(CartService.class);
    this.cartController = new CartController(this.cartService);

    this.mvc = MockMvcBuilders.standaloneSetup(cartController).build();
  }

  @Test
  public void createNewShoppingCart() throws Exception {
    // setup
    ShoppingCart cart = HelperUtil.createDummyShoppingCart();
    when(this.cartService.createNewShoppingCart()).thenReturn(cart);

    // execute & verify
    this.mvc.perform(get("/carts/create"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(cart.getId()))
        .andDo(print());
  }


}
