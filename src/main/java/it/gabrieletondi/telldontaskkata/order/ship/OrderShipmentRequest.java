package it.gabrieletondi.telldontaskkata.order.ship;

public class OrderShipmentRequest {
    private int orderId;

    public OrderShipmentRequest(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }
}
