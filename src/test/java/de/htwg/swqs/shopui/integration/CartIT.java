package de.htwg.swqs.shopui.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwg.swqs.cart.service.CartService;
import de.htwg.swqs.cart.service.CartServiceImpl;
import de.htwg.swqs.catalog.service.CatalogService;
import de.htwg.swqs.shopui.HelperUtil;
import de.htwg.swqs.shopui.controller.CartController;
import de.htwg.swqs.shopui.util.ItemWrapper;
import javax.servlet.http.Cookie;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class CartIT {

  private MockMvc mvc;
  private CatalogService catalogService;

  @Before
  public void setUp() throws Exception {

    this.catalogService = mock(CatalogService.class);
    CartService cartService = new CartServiceImpl(this.catalogService);
    CartController cartController = new CartController(cartService);

    // because we use the standalone version of the mock mvc builder we need also a viewresolver
    // https://myshittycode.com/2014/01/17/mockmvc-circular-view-path-view-would-dispatch-back-to-the-current-handler-url-view-again/
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setPrefix("/resources/templates");
    viewResolver.setSuffix(".html");
    this.mvc = MockMvcBuilders
        .standaloneSetup(cartController)
        .setViewResolvers(viewResolver)
        .build();

  }

  @Test
  public void createNewCartAndGetIdReturned() throws Exception {

    // execute
    MvcResult result = this.mvc.perform(get("/carts/create"))
        .andExpect(status().isOk())
        .andReturn();
    long idFromCart = Long.parseLong(result.getResponse().getContentAsString());

    // verify
    assertTrue(idFromCart > 0);

  }

  @Test
  public void getShoppingCartByIdSuccessful() throws Exception {

    MvcResult result = this.mvc.perform(get("/carts/create")).andReturn();
    String idFromCart = result.getResponse().getContentAsString();

    // execute & verify
    this.mvc.perform(get("/carts/get")
        .cookie(new Cookie("cart-id", idFromCart)))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idFromCart))
        .andDo(print());
  }

  @Test
  public void addItemToShoppingCart() throws Exception {

    // create new cart
    MvcResult result = this.mvc.perform(get("/carts/create"))
        .andExpect(status().isOk())
        .andReturn();
    long idFromCart = Long.parseLong(result.getResponse().getContentAsString());

    ItemWrapper[] requestWrappers = addItemsToCart(idFromCart);

    // verify
    this.mvc.perform(get("/carts/get")
        .cookie(new Cookie("cart-id", String.valueOf(idFromCart))))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idFromCart))
        .andExpect(MockMvcResultMatchers.jsonPath("$.itemsInShoppingCart[0].quantity")
            .value(requestWrappers[0].getQuantity()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.itemsInShoppingCart[0].product.id")
            .value(requestWrappers[0].getProductId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.itemsInShoppingCart[1].quantity")
            .value(requestWrappers[1].getQuantity()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.itemsInShoppingCart[1].product.id")
            .value(requestWrappers[1].getProductId()))
        .andDo(print());
  }


  @Test
  public void clearShoppingCartSuccessful() throws Exception {
    // create new cart
    MvcResult result = this.mvc.perform(get("/carts/create"))
        .andExpect(status().isOk())
        .andReturn();
    long idFromCart = Long.parseLong(result.getResponse().getContentAsString());

    // insert two items to cart
    addItemsToCart(idFromCart);

    // verify cart is empty
    this.mvc.perform(get("/carts/clear")
        .cookie(new Cookie("cart-id", String.valueOf(idFromCart))))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idFromCart))
        .andExpect(MockMvcResultMatchers.jsonPath("$.itemsInShoppingCart", hasSize(0)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.cartTotalSum").value("0.0"))
        .andDo(print());
  }


  @Test
  public void removeItemCompleteFromShoppingCart() throws Exception {
    // create new cart
    MvcResult result = this.mvc.perform(get("/carts/create"))
        .andExpect(status().isOk())
        .andReturn();
    long idFromCart = Long.parseLong(result.getResponse().getContentAsString());

    // insert two items to cart
    ItemWrapper[] requestWrappers = addItemsToCart(idFromCart);

    // convert the java object to json string
    ObjectMapper mapper = new ObjectMapper();
    String itemWrapperOneJson = mapper.writeValueAsString(requestWrappers[0]);

    // execute & verify
    this.mvc.perform(post("/carts/remove")
        .cookie(new Cookie("cart-id", String.valueOf(idFromCart)))
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
        .content(itemWrapperOneJson))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idFromCart))
        .andExpect(MockMvcResultMatchers.jsonPath("$.itemsInShoppingCart", hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.itemsInShoppingCart[0].quantity")
            .value(requestWrappers[1].getQuantity()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.itemsInShoppingCart[0].product.id")
            .value(requestWrappers[1].getProductId()))
        .andDo(print());
  }

  /**
   * Helper method for creating new cart and adding new items to this cart. Used for the
   * addItemToShoppingCart(), clearShoppingCartSuccessful() and removeItemFromShoppingCart()
   * function.
   */
  private ItemWrapper[] addItemsToCart(long idFromCart) throws Exception {

    // first item  (to clear empty cart is not so difficult)
    ItemWrapper itemWrapperOne = HelperUtil.createDummyItemRequestWrapper(12L);
    when(this.catalogService.getProductById(itemWrapperOne.getProductId()))
        .thenReturn(HelperUtil.createDummyProduct(itemWrapperOne.getProductId()));

    // convert the java object to json string
    ObjectMapper mapper = new ObjectMapper();
    String itemWrapperOneJson = mapper.writeValueAsString(itemWrapperOne);

    // add first item to cart
    this.mvc.perform(post("/carts/add")
        .cookie(new Cookie("cart-id", String.valueOf(idFromCart)))
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
        .content(itemWrapperOneJson))
        .andExpect(status().isOk());

    // second item
    ItemWrapper itemWrapperTwo = HelperUtil.createDummyItemRequestWrapper(24L);
    when(this.catalogService.getProductById(itemWrapperTwo.getProductId()))
        .thenReturn(HelperUtil.createDummyProduct(itemWrapperTwo.getProductId()));
    String itemWrapperTwoJson = mapper.writeValueAsString(itemWrapperTwo);

    // add second item to cart
    this.mvc.perform(post("/carts/add")
        .cookie(new Cookie("cart-id", String.valueOf(idFromCart)))
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
        .content(itemWrapperTwoJson))
        .andExpect(status().isOk());

    return new ItemWrapper[]{itemWrapperOne, itemWrapperTwo};
  }
}
