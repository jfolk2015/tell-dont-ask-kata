package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private String currency;
    private List<OrderItem> items;
    private OrderStatus status;
    private int id;

    public BigDecimal getTotal() {
        return items.stream()
                .map(OrderItem::getTaxedAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getTax() {
        return items.stream()
                .map(OrderItem::getTax)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addItem(OrderItem orderItem) {
        this.items.add(orderItem);
    }
}
