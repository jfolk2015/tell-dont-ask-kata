package it.gabrieletondi.telldontaskkata.order;

import it.gabrieletondi.telldontaskkata.order.approve.ApprovedOrderCannotBeRejectedException;
import it.gabrieletondi.telldontaskkata.order.approve.OrderApprovalRequest;
import it.gabrieletondi.telldontaskkata.order.approve.RejectedOrderCannotBeApprovedException;
import it.gabrieletondi.telldontaskkata.order.approve.ShippedOrdersCannotBeChangedException;
import it.gabrieletondi.telldontaskkata.order.ship.OrderCannotBeShippedException;
import it.gabrieletondi.telldontaskkata.order.ship.OrderCannotBeShippedTwiceException;

import java.math.BigDecimal;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.order.OrderStatus.*;

public class Order {
    private final int id;
    private OrderStatus status;
    private final String currency;
    private final List<OrderItem> items;

    public Order(int id, OrderStatus orderStatus, String currency, List<OrderItem> orderItems) {
        this.id = id;
        this.status = orderStatus;
        this.currency = currency;
        this.items = orderItems;
    }

    public int getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getCurrency() {
        return currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return items.stream()
                .map(OrderItem::getTaxedAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getTax() {
        return items.stream()
                .map(OrderItem::getTax)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public void approve(OrderApprovalRequest request) {
        if (status.equals(OrderStatus.SHIPPED)) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        if (request.isApproved() && status.equals(OrderStatus.REJECTED)) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        if (!request.isApproved() && status.equals(OrderStatus.APPROVED)) {
            throw new ApprovedOrderCannotBeRejectedException();
        }

        this.status = request.isApproved() ? OrderStatus.APPROVED : OrderStatus.REJECTED;
    }

    public void prepareToShip() {
        if (status.equals(CREATED) || status.equals(REJECTED)) {
            throw new OrderCannotBeShippedException();
        }

        if (status.equals(SHIPPED)) {
            throw new OrderCannotBeShippedTwiceException();
        }

        status = SHIPPED;
    }
}
