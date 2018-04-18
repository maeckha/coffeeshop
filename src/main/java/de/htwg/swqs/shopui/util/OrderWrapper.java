package de.htwg.swqs.shopui.util;

import de.htwg.swqs.order.model.CustomerInfo;
import java.util.Currency;

public class OrderWrapper {

  private CustomerInfo customerInfo;
  private Currency currency;

  public OrderWrapper() {
  }

  public CustomerInfo getCustomerInfo() {
    return customerInfo;
  }

  public void setCustomerInfo(CustomerInfo customerInfo) {
    this.customerInfo = customerInfo;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }
}
