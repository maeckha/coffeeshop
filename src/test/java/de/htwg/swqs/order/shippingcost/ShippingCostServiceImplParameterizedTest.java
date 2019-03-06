package de.htwg.swqs.order.shippingcost;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Demonstration of a junit parameterized test to test various equivalence classes of the shipping
 * cost service.
 */
@RunWith(Parameterized.class)
public class ShippingCostServiceImplParameterizedTest {

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
            {"DE", new BigDecimal("100"), 250,  new BigDecimal("3.89")},
            {"DE", new BigDecimal("100"), 4000,  new BigDecimal("5.99")},
            {"DE", new BigDecimal("100"), 7000,  new BigDecimal("8.49")},
            {"DE", new BigDecimal("100"), 12000,  new BigDecimal("16.49")},
            {"DE", new BigDecimal("100"), 20000,  new BigDecimal("16.49")},
    });
  }

  // define the parameters of the test as fields
  private String currencyISO;
  private BigDecimal costTotal;
  private int totalWeight;

  private BigDecimal expectedShippingCosts;
  private ShippingCostService shippingCostService;

  /**
   * Constructor to initialize the fields with the values from the data collection
   */
  public ShippingCostServiceImplParameterizedTest(String currencyISO, BigDecimal totalCost, int totalWeight, BigDecimal expectedShippingCosts) {

    this.shippingCostService = new ShippingCostServiceImpl();
    this.currencyISO = currencyISO;
    // in this test we have always just 1 product in the item list
    this.costTotal = totalCost;
    this.totalWeight = totalWeight;
    this.expectedShippingCosts = expectedShippingCosts;
  }


  /**
   * Just one single test method to test the shipping cost calculations with all cases.
   */
  @Test
  public void test() {
    assertEquals(expectedShippingCosts, shippingCostService
        .calculateShippingCosts(this.currencyISO, this.costTotal, this.totalWeight));
  }
}
