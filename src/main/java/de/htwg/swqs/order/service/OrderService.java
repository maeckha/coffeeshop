package de.htwg.swqs.order.service;

import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.model.Order;
import de.htwg.swqs.order.model.OrderItem;
import de.htwg.swqs.order.util.OrderNotFoundException;
import java.util.Currency;
import java.util.List;

public interface OrderService {

  Order persistOrder(Order order);

  Order getOrderById(long id) throws OrderNotFoundException;

  Order createOrder(CustomerInfo customerInfo, List<OrderItem> orderItems, Currency currency);

}
