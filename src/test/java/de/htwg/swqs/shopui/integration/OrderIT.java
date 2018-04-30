package de.htwg.swqs.shopui.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.service.CartService;
import de.htwg.swqs.order.mail.EmailService;
import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.payment.CurrencyConverterService;
import de.htwg.swqs.order.repository.OrderRepository;
import de.htwg.swqs.order.service.OrderService;
import de.htwg.swqs.order.service.OrderServiceImpl;
import de.htwg.swqs.order.shippingcost.ShippingCostService;
import de.htwg.swqs.shopui.HelperUtil;
import de.htwg.swqs.shopui.controller.OrderController;
import de.htwg.swqs.shopui.util.OrderWrapper;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import javax.servlet.http.Cookie;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderIT {

  private MockMvc mvc;

  // spring can find and mock available beans
  @MockBean
  private CurrencyConverterService currencyConverterService;
  @MockBean
  private EmailService emailService;
  @MockBean
  private CartService cartService;

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private ShippingCostService shippingCostService;

  private OrderService orderService;
  private OrderController orderController;

  @Before
  public void setUp() throws Exception {

    when(this.currencyConverterService
        .convertTo(any(Currency.class), any(Currency.class), any(BigDecimal.class)))
        .thenReturn(new BigDecimal("99.99"));
    when(this.cartService.getShoppingCart(anyLong())).thenReturn(mock(ShoppingCart.class));

    this.orderService = new OrderServiceImpl(
        this.shippingCostService,
        this.orderRepository,
        this.currencyConverterService,
        this.emailService
    );
    this.orderController = new OrderController(this.orderService, this.cartService);

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

    // setup is done by the setUp() function

    // execute & verify
    this.mvc.perform(get("/order")
        .accept(MediaType.TEXT_HTML)
        .cookie(new Cookie("cart-id", "1")))
        .andExpect(model().attributeExists("title", "cart"))
        .andExpect(view().name("order-create"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void createNewOrderAndRetrieveOrderObject() throws Exception {

    // setup
    ShoppingCart dummyCart = HelperUtil.createDummyShoppingCart();

    // with the doReturn statement we can specify the return behaviour of the cart service
    // https://stackoverflow.com/a/40247083
    doReturn(dummyCart)
        .when(cartService)
        .getShoppingCart(1L);

    CustomerInfo customerInfo = HelperUtil.createDummyCustomerInfo();
    Currency currency = Currency.getInstance("EUR");
    OrderWrapper orderWrapper = new OrderWrapper();
    orderWrapper.setCurrency(currency);
    orderWrapper.setCustomerInfo(customerInfo);

    // convert the java object to json string
    ObjectMapper mapper = new ObjectMapper();
    String jsonRequestBody = mapper.writeValueAsString(orderWrapper);

    // execute & verify
    MvcResult result = this.mvc.perform(post("/order")
        .cookie(new Cookie("cart-id", "1"))
        .content(jsonRequestBody)
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        //.andExpect(content().json("{'json': 'response'}"))
        //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        //.andExpect(MockMvcResultMatchers.jsonPath("$.customerInfo.email").value("max@muster.de"));
        .andExpect(view().name("order-validate"))
        .andExpect(model().attributeExists("title", "order"))
        .andReturn();

    Map<String, Object> modelAndViewMap = result.getModelAndView().getModel();
    Order createdOrder = (Order) modelAndViewMap.get("order");

    Assert.assertTrue(customerInfo.compareTo(createdOrder.getCustomerInfo()) == 0);
    Assert.assertEquals(dummyCart.getItemsInShoppingCart().size(),
        createdOrder.getOrderItems().size());
    Assert.assertEquals(currency.getCurrencyCode(), createdOrder.getCostTotal().getCurrency());
  }
}
