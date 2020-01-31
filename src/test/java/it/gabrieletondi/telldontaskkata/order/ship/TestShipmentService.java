package it.gabrieletondi.telldontaskkata.order.ship;

import it.gabrieletondi.telldontaskkata.order.Order;

public class TestShipmentService implements ShipmentService {
    private Order shippedOrder = null;

    public Order getShippedOrder() {
        return shippedOrder;
    }

    @Override
    public void ship(Order order) {
        this.shippedOrder = order;
    }
}
