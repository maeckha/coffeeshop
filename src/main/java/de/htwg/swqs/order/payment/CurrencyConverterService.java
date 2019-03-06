package de.htwg.swqs.order.payment;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

public interface CurrencyConverterService {

  BigDecimal convertTo(Currency currencyFrom, Currency currencyTo, BigDecimal amount)
      throws IOException;

}
