package de.htwg.swqs.order.model;

import java.math.BigDecimal;
import java.util.Currency;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Cost {

  @Column(precision = 19, scale = 4)
  private BigDecimal amount;

  // the ISO 3166 string for the currency
  private String currency;

  /**
   * Constructor with parameters to create a completed cost object.
   *
   * @param amount The amount of the costs
   * @param currency The currency in which the costs are represented
   */
  public Cost(BigDecimal amount, Currency currency) {
    super();
    this.amount = amount;
    this.currency = currency.getCurrencyCode();
  }

  public Cost() {
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getCurrency() {
    return this.currency;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((amount == null) ? 0 : amount.hashCode());
    result = prime * result + ((currency == null) ? 0 : currency.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Cost other = (Cost) obj;
    if (amount == null) {
      if (other.amount != null) {
        return false;
      }
    } else if (!amount.equals(other.amount)) {
      return false;
    }
    if (currency == null) {
      if (other.currency != null) {
        return false;
      }
    } else if (!currency.equals(other.currency)) {
      return false;
    }
    return true;
  }


}