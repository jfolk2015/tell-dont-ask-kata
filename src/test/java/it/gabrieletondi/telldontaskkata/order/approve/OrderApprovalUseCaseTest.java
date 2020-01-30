package it.gabrieletondi.telldontaskkata.order.approve;

import it.gabrieletondi.telldontaskkata.order.Order;
import it.gabrieletondi.telldontaskkata.order.OrderStatus;
import it.gabrieletondi.telldontaskkata.order.TestOrderRepository;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderApprovalUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final OrderApprovalUseCase useCase = new OrderApprovalUseCase(orderRepository);

    @Test
    public void approvedExistingOrder() {
        Order initialOrder = new Order(1, OrderStatus.CREATED, "", emptyList());
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(true);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.getStatus(), is(OrderStatus.APPROVED));
    }

    @Test
    public void rejectedExistingOrder() {
        Order initialOrder = new Order(1, OrderStatus.CREATED, "", emptyList());
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(false);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.getStatus(), is(OrderStatus.REJECTED));
    }

    @Test(expected = RejectedOrderCannotBeApprovedException.class)
    public void cannotApproveRejectedOrder() {
        Order initialOrder = new Order(1, OrderStatus.REJECTED, "", emptyList());
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(true);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ApprovedOrderCannotBeRejectedException.class)
    public void cannotRejectApprovedOrder() {
        Order initialOrder = new Order(1, OrderStatus.APPROVED, "", emptyList());
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(false);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ShippedOrdersCannotBeChangedException.class)
    public void shippedOrdersCannotBeApproved() {
        Order initialOrder = new Order(1, OrderStatus.SHIPPED, "", emptyList());
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(true);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ShippedOrdersCannotBeChangedException.class)
    public void shippedOrdersCannotBeRejected() {
        Order initialOrder = new Order(1, OrderStatus.SHIPPED, "", emptyList());
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(false);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }
}
