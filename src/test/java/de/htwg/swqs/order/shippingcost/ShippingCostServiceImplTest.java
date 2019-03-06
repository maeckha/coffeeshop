package de.htwg.swqs.order.shippingcost;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class ShippingCostServiceImplTest {
	private ShippingCostServiceImpl shippingCostService;

    @Before
    public void setUp() {
        this.shippingCostService = new ShippingCostServiceImpl();
    }

    @Test
    public void calculateShippingCostsLowWeightToDE() {
        // execute
        BigDecimal costs = this.shippingCostService.calculateShippingCosts("DE",new BigDecimal("10.0"),250);

        // verify the calculated shipping cost is greater than zero
        assertEquals(new BigDecimal("3.89"), costs);
    }
    
    @Test
    public void calculateShippingCostsLowWeightToCH() {
        // execute
        BigDecimal costs = this.shippingCostService.calculateShippingCosts("CH",new BigDecimal("10.0"),250);

        // verify the calculated shipping cost is greater than zero
        assertEquals(new BigDecimal("12.70"), costs);
    }
    
    @Test
    public void calculateShippingCostsLowWeightToAT() {
        // execute
        BigDecimal costs = this.shippingCostService.calculateShippingCosts("AT",new BigDecimal("10.0"),250);

        // verify the calculated shipping cost is greater than zero
        assertEquals(new BigDecimal("8.89"), costs);
    }
    
    @Test
    public void calculateShippingCostsMediumWeightToDE() {
        // execute
        BigDecimal costs1 = this.shippingCostService.calculateShippingCosts("DE",new BigDecimal("190.0"), 250);
        BigDecimal costs2 = this.shippingCostService.calculateShippingCosts("DE",new BigDecimal("210.0"), 250);
        BigDecimal costs3 = this.shippingCostService.calculateShippingCosts("CH",new BigDecimal("290.0"), 250);
        BigDecimal costs4 = this.shippingCostService.calculateShippingCosts("CH",new BigDecimal("310.0"), 250);

        // verify the calculated shipping cost is greater than zero
        assertEquals(new BigDecimal("3.89"), costs1);
        assertEquals(new BigDecimal("0.00"), costs2);
        assertEquals(new BigDecimal("12.70"), costs3);
        assertEquals(new BigDecimal("0.00"), costs4);
    }
}
