package de.htwg.swqs.shopui.controller;

import static org.junit.Assert.assertEquals;
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
import de.htwg.swqs.shopui.HelperUtil;
import javax.servlet.http.Cookie;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * In this test suite we only test the CartViewController, the DOC are just a mock. We don't use the
 * html sites for navigation, just the controller methods.
 */
public class CartViewControllerTest {

  private MockMvc mvc;
  private CartService cartService;
  private CartViewController cartViewController;
  private ShoppingCart shoppingCart;

  @Before
  public void setUp() {
    this.cartService = mock(CartService.class);
    this.cartViewController = new CartViewController(cartService);
    this.shoppingCart = HelperUtil.createDummyShoppingCart();

    this.mvc = MockMvcBuilders.standaloneSetup(cartViewController).build();

  }

  @Test
  public void testCartViewControllerReturnsPageAttachedWithShoppingCart() throws Exception {
    // setup
    when(cartService.getShoppingCart(anyLong())).thenReturn(this.shoppingCart);

    // execute & verify
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
  public void testCartViewControllerResetsCartRedirectsToShoppingCart() throws Exception {
    // setup
    when(cartService.getShoppingCart(anyLong())).thenReturn(this.shoppingCart);

    // execute & verify
    this.mvc.perform(get("/clear-cart")
            .cookie(new Cookie("cart-id", "1")))
            .andExpect(status().is3xxRedirection())
            .andDo(print());
  }


  @Test
  public void testCartViewControllerWithoutCookieReturnsError() throws Exception {
    // setup
    when(cartService.getShoppingCart(anyLong())).thenReturn(this.shoppingCart);

    // execute & verify
    this.mvc.perform(get("/show-cart")
        .accept(MediaType.TEXT_HTML))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }
}
