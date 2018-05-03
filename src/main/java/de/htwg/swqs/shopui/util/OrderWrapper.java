package de.htwg.swqs.shopui.util;

import de.htwg.swqs.order.model.CustomerInfo;
import java.util.Currency;
import javax.validation.constraints.NotNull;

public class OrderWrapper {

  @NotNull
  private CustomerInfo customerInfo;
  @NotNull
  private Currency currency;

  public OrderWrapper() {
  }

  public OrderWrapper(
      @NotNull CustomerInfo customerInfo,
      @NotNull Currency currency) {
    this.customerInfo = customerInfo;
    this.currency = currency;
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
