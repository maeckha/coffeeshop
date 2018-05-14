package de.htwg.swqs.shopui.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.model.ShoppingCartItem;
import de.htwg.swqs.cart.service.CartService;
import de.htwg.swqs.order.mail.EmailService;
import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.payment.CurrencyConverterService;
import de.htwg.swqs.order.payment.PaymentMethodService;
import de.htwg.swqs.order.repository.OrderRepository;
import de.htwg.swqs.order.service.OrderService;
import de.htwg.swqs.order.service.OrderServiceImpl;
import de.htwg.swqs.order.shippingcost.ShippingCostService;
import de.htwg.swqs.shopui.HelperUtil;
import de.htwg.swqs.shopui.controller.OrderController;
import de.htwg.swqs.shopui.util.OrderWrapper;
import gherkin.lexer.He;
import io.florianlopes.spring.test.web.servlet.request.MockMvcRequestBuilderUtils;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * We don't want to use the external interfaces of the order service. Instead we just focus on the
 * interaction between the OrderController and the OrderService.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class OrderIT {

  private MockMvc mvc;

  // spring can find and mock available beans
  @MockBean
  private CurrencyConverterService currencyConverterService;
  @MockBean
  private EmailService emailService;

  @Autowired
  private CartService cartService;
  @Autowired
  private PaymentMethodService paymentMethodService;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private ShippingCostService shippingCostService;

  private OrderService orderService;
  private OrderController orderController;

  @Before
  public void setUp() throws Exception {

    // specify the behaviour of the mocked external api
    when(this.currencyConverterService
        .convertTo(any(Currency.class), any(Currency.class), any(BigDecimal.class)))
        .thenReturn(new BigDecimal("99.99"));

    this.orderService = new OrderServiceImpl(
        this.shippingCostService,
        this.orderRepository,
        this.currencyConverterService,
        this.emailService
    );
    this.orderController = new OrderController(this.orderService, this.cartService,
        this.paymentMethodService);

    // because we use the standalone version of the mock mvc builder we need also a viewresolver
    // https://myshittycode.com/2014/01/17/mockmvc-circular-view-path-view-would-dispatch-back-to-the-current-handler-url-view-again/
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setPrefix("/resources/templates");
    viewResolver.setSuffix(".html");
    this.mvc = MockMvcBuilders.standaloneSetup(this.orderController).setViewResolvers(viewResolver)
        .build();

  }

  @Test
  public void showOrderPage() throws Exception {

    // insert some initial data to the cart service
    ShoppingCart cart = this.cartService.createNewShoppingCart();
    List<ShoppingCartItem> itemList = HelperUtil.createDummyShoppingItemList();
    cart.setItemsInShoppingCart(itemList);

    // execute & verify
    this.mvc.perform(get("/order")
        .accept(MediaType.TEXT_HTML)
        .cookie(new Cookie("cart-id", String.valueOf(cart.getId()))))
        .andExpect(model().attributeExists("title", "cart"))
        .andExpect(view().name("order-create"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void createNewOrderAndRetrieveOrderObject() throws Exception {

    // setup
    // insert some initial data to the cart service
    ShoppingCart cart = this.cartService.createNewShoppingCart();
    List<ShoppingCartItem> itemList = HelperUtil.createDummyShoppingItemList();
    cart.setItemsInShoppingCart(itemList);

    CustomerInfo customerInfo = HelperUtil.createDummyCustomerInfo();
    Currency currency = Currency.getInstance("EUR");
    OrderWrapper orderWrapper = new OrderWrapper();
    orderWrapper.setCurrency(currency);
    orderWrapper.setCustomerInfo(customerInfo);

    MvcResult result = this.mvc.perform(MockMvcRequestBuilderUtils.postForm("/order", orderWrapper)
        .cookie(new Cookie("cart-id", String.valueOf(cart.getId()))))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(view().name("order-validate"))
        .andExpect(model().attributeExists("title", "order"))
        .andReturn();

    Map<String, Object> modelAndViewMap = result.getModelAndView().getModel();
    Order createdOrder = (Order) modelAndViewMap.get("order");

    Assert.assertTrue(customerInfo.compareTo(createdOrder.getCustomerInfo()) == 0);
    Assert.assertEquals(cart.getItemsInShoppingCart().size(),
        createdOrder.getOrderItems().size());
    Assert.assertEquals(currency.getCurrencyCode(), createdOrder.getCostTotal().getCurrency());
  }
}
