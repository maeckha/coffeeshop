package de.htwg.swqs.order.shippingcost;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class ShippingCostServiceImpl implements ShippingCostService {

    private static final String DEFAULT_COUNTRY = "DE";
    private static final BigDecimal FREE_SHIPPING_START_DE = new BigDecimal("200.00");
    private static final BigDecimal FREE_SHIPPING_START_CH = new BigDecimal("300.00");

    /**
     * Calculates the shipping costs for a given list of items and a CustomerInfo.
     * If the customer lives outside of the default country, an additional charge
     * for international shipping will be added.
     *
     * @param countryISO destination country
     * @return A BigDecimal containing the shipping costs
     */
    public BigDecimal calculateShippingCosts(String countryISO, BigDecimal totalCostOfItems, int weight) {

        if (checkForFreeShipping(countryISO, totalCostOfItems)) {
            return new BigDecimal("0.00");
        }

        BigDecimal shippingCost = null;
        // costs are taken from the dhl price list for 'Paket' delivery
        // https://www.dhl.de/de/privatkunden/preise/preise-national.html
        if (countryISO.equals(DEFAULT_COUNTRY)) {
            if (weight <= 1000) {
                shippingCost = new BigDecimal("3.89");
            } else if (weight <= 2000) {
                shippingCost = new BigDecimal("4.39");
            } else if (weight <= 5000) {
                shippingCost = new BigDecimal("5.99");
            } else if (weight <= 10000) {
                shippingCost = new BigDecimal("8.49");
            } else if (weight <= 31500) {
                shippingCost = new BigDecimal("16.49");
            }
        }
        if (countryISO.equals("CH")) {
            if (weight <= 2000) {
                shippingCost = new BigDecimal("12.70");
            } else if (weight <= 5000) {
                shippingCost = new BigDecimal("26.90");
            } else if (weight <= 10000) {
                shippingCost = new BigDecimal("34.99");
            } else if (weight <= 20000) {
                shippingCost = new BigDecimal("48.99");
            } else if (weight <= 31500) {
                shippingCost = new BigDecimal("55.99");
            }
        }
        if (countryISO.equals("AT")) {
            if (weight <= 2000) {
                shippingCost = new BigDecimal("8.89");
            } else if (weight <= 5000) {
                shippingCost = new BigDecimal("15.99");
            } else if (weight <= 10000) {
                shippingCost = new BigDecimal("20.99");
            } else if (weight <= 20000) {
                shippingCost = new BigDecimal("31.99");
            } else if (weight <= 31500) {
                shippingCost = new BigDecimal("44.99");
            }
        }
        return shippingCost;
    }

    private boolean checkForFreeShipping(String countryISO, BigDecimal totalCostOfItems) {
        if (totalCostOfItems.compareTo(FREE_SHIPPING_START_DE) >= 0 && countryISO.equals(DEFAULT_COUNTRY)) {
            return true;
        }
        if (totalCostOfItems.compareTo(FREE_SHIPPING_START_CH) >= 0 && countryISO.equals("CH")) {
            return true;
        }
        return false;
    }
}
