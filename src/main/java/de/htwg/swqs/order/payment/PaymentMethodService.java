package de.htwg.swqs.order.payment;

import de.htwg.swqs.order.model.Cost;
import de.htwg.swqs.order.model.CustomerInfo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface PaymentMethodService {

  /**
   * Returns the list of accepted payment methods for a given person, living at a given address for
   * a given amount.
   *
   * @param customerInfo Customer for which the payment method should be determined.
   * @param cost Cost of the order, includes the currency.
   * @return The accepted payment methods for the given customer and cost.
   */
  List<PaymentMethod> getAcceptedMethods(CustomerInfo customerInfo, Cost cost);
}