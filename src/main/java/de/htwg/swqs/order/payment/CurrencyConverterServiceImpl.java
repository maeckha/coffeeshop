package de.htwg.swqs.order.payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Currency;
import org.springframework.stereotype.Service;


@Service
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

  @Override
  public BigDecimal convertTo(Currency currencyFrom, Currency currencyTo, BigDecimal amount)
      throws IOException {

    if (currencyFrom.equals(currencyTo)) {
      return amount;
    }

    if (amount.compareTo(new BigDecimal("0.00")) < 1) {
      // the amount is 0 or negative
      throw new IllegalArgumentException("The passed amount must be greater than 0");
    }

    String parameters = "q=" + currencyFrom + "_" + currencyTo + "&compact=ultra";

    BigDecimal exchangeRate = callExternalExchangeService(parameters);

    return amount.multiply(exchangeRate);
  }

  private BigDecimal callExternalExchangeService(String parameters) throws IOException {

    URL url = new URL("https://free.currencyconverterapi.com/api/v5/convert" + "?" + parameters);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    connection.setDoOutput(true);

    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;
    StringBuilder content = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }
    in.close();
    connection.disconnect();
    StringBuilder jsonResponse = new StringBuilder(content.toString());

    System.out.println("Retrieved response = " + jsonResponse.toString());

    jsonResponse.delete(0, 11);
    jsonResponse.deleteCharAt(jsonResponse.length() - 1);

    return new BigDecimal(jsonResponse.toString());
  }
}
