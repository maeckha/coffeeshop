package de.htwg.swqs.order.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.htwg.swqs.order.model.Cost;
import de.htwg.swqs.order.model.CustomerInfo;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class PaymentMethodServiceTest {

  /**
   * validPostcodes = {"78462", "78464", "78465", "78467"};
   * blacklist = {"Max/Mustermann"};
   * limit = new BigDecimal("1000.00");
   */
  private PaymentMethodService paymentMethodService;
  private CustomerInfo customerInfo;

  @Before
  public void setUp() {
    this.paymentMethodService = new PaymentMethodServiceImpl();

    // thats the unrelevant part of the customer info
    this.customerInfo = new CustomerInfo();
    this.customerInfo.setEmail("max@muster.de");
    this.customerInfo.setStreet("Hauptstraße 3");
    this.customerInfo.setCity("Konstanz");
    this.customerInfo.setIsoCountryCode("DE");
  }

  @Test
  public void getPaymentMethodsForValidCustomerAndCityOutsideConstanceInLimit() {

    // setup
    this.customerInfo.setPostcode("71849");
    this.customerInfo.setFirstname("John");
    this.customerInfo.setSurname("Doe");
    Cost cost = new Cost();
    cost.setAmount(new BigDecimal("42.00"));
    cost.setCurrency("DE");

    // execute
    List<PaymentMethod> paymentMethods = this.paymentMethodService.getAcceptedMethods(this.customerInfo, cost);

    // verify
    assertEquals(PaymentMethod.prePayment, paymentMethods.get(0));

  }

  @Test
  public void getPaymentMethodsForValidCustomerInConstanceInLimit() {

    // setup
    this.customerInfo.setPostcode("78462");
    this.customerInfo.setFirstname("John");
    this.customerInfo.setSurname("Doe");
    Cost cost = new Cost();
    cost.setAmount(new BigDecimal("42.00"));
    cost.setCurrency("DE");

    // execute
    List<PaymentMethod> paymentMethods = this.paymentMethodService.getAcceptedMethods(this.customerInfo, cost);

    // verify
    assertTrue(paymentMethods.contains(PaymentMethod.prePayment));
    assertTrue(paymentMethods.contains(PaymentMethod.creditCard));
    assertTrue(paymentMethods.contains(PaymentMethod.invoice));

  }

  @Test
  public void getPaymentMethodsForValidCustomerInConstanceOverLimit() {

    // setup
    this.customerInfo.setPostcode("78462");
    this.customerInfo.setFirstname("John");
    this.customerInfo.setSurname("Doe");
    Cost cost = new Cost();
    cost.setAmount(new BigDecimal("1200.00"));
    cost.setCurrency("DE");

    // execute
    List<PaymentMethod> paymentMethods = this.paymentMethodService.getAcceptedMethods(this.customerInfo, cost);

    // verify
    assertTrue(paymentMethods.contains(PaymentMethod.prePayment));
    assertTrue(paymentMethods.contains(PaymentMethod.creditCard));

  }

  @Test
  public void getPaymentMethodsForInvalidCustomer() {

    // setup
    this.customerInfo.setPostcode("78462");
    this.customerInfo.setFirstname("Max");
    this.customerInfo.setSurname("Mustermann");
    Cost cost = new Cost();
    cost.setAmount(new BigDecimal("33.00"));
    cost.setCurrency("DE");

    // execute
    List<PaymentMethod> paymentMethods = this.paymentMethodService.getAcceptedMethods(this.customerInfo, cost);

    // verify
    assertTrue(paymentMethods.isEmpty());
  }
}
