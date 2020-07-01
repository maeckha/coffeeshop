package de.htwg.swqs.order.model;

import java.math.BigDecimal;

class ermMwstUmrechnung {

    public Cost Umrechnung(Cost amount) {
        double amount1;
        amount1 = (double) amount / 1.11214953271;
        ((Cost) amount1);
        return amount1;
    }

}
