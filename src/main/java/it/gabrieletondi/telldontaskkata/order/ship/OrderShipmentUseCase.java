package it.gabrieletondi.telldontaskkata.order.ship;

import it.gabrieletondi.telldontaskkata.order.Order;
import it.gabrieletondi.telldontaskkata.order.OrderRepository;

public class OrderShipmentUseCase {
    private final OrderRepository orderRepository;
    private final ShipmentService shipmentService;

    public OrderShipmentUseCase(OrderRepository orderRepository, ShipmentService shipmentService) {
        this.orderRepository = orderRepository;
        this.shipmentService = shipmentService;
    }

    public void run(OrderShipmentRequest request) {
        final Order order = orderRepository.getById(request.getOrderId());
        order.prepareToShip();
        shipmentService.ship(order);
        orderRepository.save(order);
    }
}
