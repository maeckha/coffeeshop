package de.htwg.swqs.order.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import de.htwg.swqs.order.mail.EmailService;
import de.htwg.swqs.order.model.Cost;
import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.model.OrderItem;
import de.htwg.swqs.order.payment.CurrencyConverterService;
import de.htwg.swqs.order.payment.PaymentMethod;
import de.htwg.swqs.order.repository.OrderRepository;
import de.htwg.swqs.order.shippingcost.ShippingCostService;
import de.htwg.swqs.order.shippingcost.ShippingCostServiceImpl;
import de.htwg.swqs.order.util.OrderNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

/**
 * Example of a hybrid fixture creation. The creation of customerInfo and shoppingCart is delegated
 * to separate helper functions, the class variables orderService, orderRepository and
 * shippingCostService are created by the init() function
 */
public class OrderServiceHybridFixtureTest {

  private OrderService orderService;
  private ShippingCostService shippingCostService;
  private OrderRepository orderRepository;
  private CurrencyConverterService currencyConverterService;
  private EmailService emailService;

  @Before
  public void init() {
    this.shippingCostService = new ShippingCostServiceImpl();
    this.orderRepository = mock(OrderRepository.class);
    this.currencyConverterService = mock(CurrencyConverterService.class);
    this.emailService = mock(EmailService.class);
    this.orderService = new OrderServiceImpl(this.shippingCostService, this.orderRepository,
        this.currencyConverterService, this.emailService);
  }


  @Test
  public void createOrder() {
    // setup
    CustomerInfo customerInfo = createDummyCustomerInfo();
    List<OrderItem> itemList = createDummyOrderItemList();
    Currency currency = Currency.getInstance("EUR");

    // execute
    Order createdOrder = this.orderService.createOrder(customerInfo, itemList, currency);

    // verify
    assertEquals(customerInfo,createdOrder.getCustomerInfo());
    assertEquals("EUR", createdOrder.getCostTotal().getCurrency());
    assertEquals(new BigDecimal("56.32"), createdOrder.getCostTotal().getAmount());
    assertEquals(new BigDecimal("4.39"), createdOrder.getCostShipping());
  }


  @Test
  public void testPersistOrder() {
    // setup
    CustomerInfo customerInfo = createDummyCustomerInfo();
    List<OrderItem> itemList = createDummyOrderItemList();
    Currency currency = Currency.getInstance("EUR");

    Order createdOrder = this.orderService.createOrder(customerInfo, itemList, currency);
    createdOrder.setId(1000L);
    when(this.orderRepository.saveAndFlush(any(Order.class))).thenReturn(createdOrder);

    // execute
    Order persistedOrder = this.orderService.persistOrder(createdOrder);

    // verify
    verify(this.orderRepository, times(1)).saveAndFlush(eq(createdOrder));
    verifyNoMoreInteractions(this.orderRepository);
    verify(this.emailService, times(1)).sendMail(any(String.class),any(String.class),any(String.class));

  }


  private CustomerInfo createDummyCustomerInfo() {
    CustomerInfo customerInfo = new CustomerInfo();
    customerInfo.setEmail("max@muster.de");
    customerInfo.setCity("Konstanz");
    customerInfo.setPostcode("78467");
    customerInfo.setStreet("Hauptstraße 3");
    customerInfo.setIsoCountryCode("DE");
    customerInfo.setFirstname("Max");
    customerInfo.setSurname("Muster");
    return customerInfo;
  }

  private List<OrderItem> createDummyOrderItemList() {
    OrderItem firstItem = new OrderItem(3,1l, new BigDecimal("3.33"),100);
    OrderItem secondItem = new OrderItem(6,2l, new BigDecimal("6.99"),200);
    return Arrays.asList(firstItem, secondItem);
  }


}
