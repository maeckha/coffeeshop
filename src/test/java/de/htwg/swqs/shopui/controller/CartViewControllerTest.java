package de.htwg.swqs.shopui.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.service.CartService;
import javax.servlet.http.Cookie;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * In this test suite we only test the real candidate for the cart service in the cart view
 * controller, the other DOC are just mock objects
 */
public class CartViewControllerTest {

  private MockMvc mvc;

  @Test
  public void testCartViewControllerReturnsPageAttachedWithShoppingCart() throws Exception {

    // setup
    CartService cartService = mock(CartService.class);
    CartViewController cartViewController = new CartViewController(cartService);
    ShoppingCart dummyCart = mock(ShoppingCart.class);
    when(cartService.getShoppingCart(anyLong())).thenReturn(dummyCart);

    this.mvc = MockMvcBuilders.standaloneSetup(cartViewController).build();

    this.mvc.perform(get("/show-cart")
        .accept(MediaType.TEXT_HTML)
        .cookie(new Cookie("cart-id", "1")))
        .andExpect(model()
            .attributeExists("title", "shoppingCart"))
        .andExpect(view().name("shoppingcart"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void testCartViewControllerWithoutCookieReturnsError() throws Exception {

  }
}
