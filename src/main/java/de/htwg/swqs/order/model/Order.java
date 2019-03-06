package de.htwg.swqs.order.model;

import de.htwg.swqs.order.payment.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "order_data")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  private CustomerInfo customerInfo;
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  private List<OrderItem> orderItems = new ArrayList<>();
  @Column(precision = 19, scale = 4)
  private BigDecimal costShipping;
  @Embedded
  private Cost costTotal;
  private LocalDate orderDate;
  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;

  public Order() {
  }

  /**
   * Parametrized constructor with all values.
   *
   * @param customerInfo Customerinfo object which corresponds to the order
   * @param orderItems A List with the order items
   * @param costShipping BigDecimal of the shipping costs, always in euro
   * @param costTotal The total cost of the order
   * @param orderDate Date when the order was created
   * @param paymentMethod The payment method chosen by the customer
   */
  public Order(CustomerInfo customerInfo, List<OrderItem> orderItems, BigDecimal costShipping,
      Cost costTotal, LocalDate orderDate, PaymentMethod paymentMethod) {
    this.customerInfo = customerInfo;
    this.orderItems = orderItems;
    this.costShipping = costShipping;
    this.costTotal = costTotal;
    this.orderDate = orderDate;
    this.paymentMethod = paymentMethod;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public CustomerInfo getCustomerInfo() {
    return customerInfo;
  }

  public void setCustomerInfo(CustomerInfo customerInfo) {
    this.customerInfo = customerInfo;
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public BigDecimal getCostShipping() {
    return costShipping;
  }

  public void setCostShipping(BigDecimal costShipping) {
    this.costShipping = costShipping;
  }

  public Cost getCostTotal() {
    return costTotal;
  }

  public void setCostTotal(Cost costTotal) {
    this.costTotal = costTotal;
  }

  public LocalDate getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(LocalDate orderDate) {
    this.orderDate = orderDate;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  @Override
  public String toString() {
    return "Order{"
        + "id=" + id
        + ", customerInfo=" + customerInfo
        + ", orderItems=" + orderItems
        + ", costShipping=" + costShipping
        + ", costTotal=" + costTotal
        + ", orderDate=" + orderDate
        + ", paymentMethod=" + paymentMethod
        + '}';
  }
}

