package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.doubles.TestShipmentService;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderShipmentTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final TestShipmentService shipmentService = new TestShipmentService();

    @Test
    public void shipApprovedOrder() {
        Order initialOrder = new Order(1, OrderStatus.APPROVED, "", emptyList());
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest(orderRepository, shipmentService);
        request.setOrderId(1);

        request.process();

        assertThat(orderRepository.getSavedOrder().getStatus(), is(OrderStatus.SHIPPED));
        assertThat(shipmentService.getShippedOrder(), is(initialOrder));
    }

    @Test(expected = OrderCannotBeShippedException.class)
    public void createdOrdersCannotBeShipped() {
        Order initialOrder = new Order(1, OrderStatus.CREATED, "", emptyList());
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest(orderRepository, shipmentService);
        request.setOrderId(1);

        request.process();

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
        assertThat(shipmentService.getShippedOrder(), is(nullValue()));
    }

    @Test(expected = OrderCannotBeShippedException.class)
    public void rejectedOrdersCannotBeShipped() {
        Order initialOrder = new Order(1, OrderStatus.REJECTED, "", emptyList());
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest(orderRepository, shipmentService);
        request.setOrderId(1);

        request.process();

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
        assertThat(shipmentService.getShippedOrder(), is(nullValue()));
    }

    @Test(expected = OrderCannotBeShippedTwiceException.class)
    public void shippedOrdersCannotBeShippedAgain() {
        Order initialOrder = new Order(1, OrderStatus.SHIPPED, "", emptyList());
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest(orderRepository, shipmentService);
        request.setOrderId(1);

        request.process();

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
        assertThat(shipmentService.getShippedOrder(), is(nullValue()));
    }
}
