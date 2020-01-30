package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class Product {
    private final String name;
    private final BigDecimal price;
    private final BigDecimal taxPercentage;

    public Product(String name, BigDecimal price, BigDecimal taxPercentage) {
        this.name = name;
        this.price = price;
        this.taxPercentage = taxPercentage;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getUnitaryTax() {
        return price.divide(valueOf(100)).multiply(taxPercentage).setScale(2, HALF_UP);
    }

    public BigDecimal getUnitaryTaxedAmount() {
        return price.add(getUnitaryTax()).setScale(2, HALF_UP);
    }
}
