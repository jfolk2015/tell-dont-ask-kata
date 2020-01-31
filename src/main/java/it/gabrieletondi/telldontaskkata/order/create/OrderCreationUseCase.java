package it.gabrieletondi.telldontaskkata.order.create;

import it.gabrieletondi.telldontaskkata.order.Order;
import it.gabrieletondi.telldontaskkata.order.OrderItem;
import it.gabrieletondi.telldontaskkata.order.OrderRepository;
import it.gabrieletondi.telldontaskkata.order.OrderStatus;
import it.gabrieletondi.telldontaskkata.product.Product;
import it.gabrieletondi.telldontaskkata.product.ProductCatalog;

import java.util.ArrayList;
import java.util.List;

public class OrderCreationUseCase {
    private final OrderRepository orderRepository;
    private final ProductCatalog productCatalog;

    public OrderCreationUseCase(OrderRepository orderRepository, ProductCatalog productCatalog) {
        this.orderRepository = orderRepository;
        this.productCatalog = productCatalog;
    }

    public void run(SellItemsRequest request) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (SellItemRequest itemRequest : request.getRequests()) {
            Product product = productCatalog.getByName(itemRequest.getProductName());

            if (product == null) {
                throw new UnknownProductException();
            }
            else {
                final OrderItem orderItem = new OrderItem(product, itemRequest.getQuantity());
                orderItems.add(orderItem);
            }
        }

        Order order = new Order(1, OrderStatus.CREATED, "EUR", orderItems);
        orderRepository.save(order);
    }
}
