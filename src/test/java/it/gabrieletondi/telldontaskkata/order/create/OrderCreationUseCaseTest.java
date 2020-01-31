package it.gabrieletondi.telldontaskkata.order.create;

import it.gabrieletondi.telldontaskkata.order.Order;
import it.gabrieletondi.telldontaskkata.order.OrderStatus;
import it.gabrieletondi.telldontaskkata.product.Product;
import it.gabrieletondi.telldontaskkata.product.InMemoryProductCatalog;
import it.gabrieletondi.telldontaskkata.order.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.product.ProductCatalog;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OrderCreationUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final ProductCatalog productCatalog = new InMemoryProductCatalog(
            Arrays.asList(
                    new Product("salad", new BigDecimal("3.56"), new BigDecimal("10")),
                    new Product("tomato", new BigDecimal("4.65"), new BigDecimal("10"))
            )
    );
    private final OrderCreationUseCase useCase = new OrderCreationUseCase(orderRepository, productCatalog);

    @Test
    public void sellMultipleItems() {
        SellItemRequest saladRequest = new SellItemRequest(2, "salad");
        SellItemRequest tomatoRequest = new SellItemRequest(3, "tomato");
        List<SellItemRequest> requests = Arrays.asList(saladRequest, tomatoRequest);
        final SellItemsRequest request = new SellItemsRequest(requests);
        request.getRequests().add(saladRequest);
        request.getRequests().add(tomatoRequest);

        useCase.run(request);

        final Order insertedOrder = orderRepository.getSavedOrder();
        assertThat(insertedOrder.getStatus(), is(OrderStatus.CREATED));
        assertThat(insertedOrder.getTotal(), is(new BigDecimal("23.20")));
        assertThat(insertedOrder.getTax(), is(new BigDecimal("2.13")));
        assertThat(insertedOrder.getCurrency(), is("EUR"));
        assertThat(insertedOrder.getItems(), hasSize(2));
        assertThat(insertedOrder.getItems().get(0).getProduct().getName(), is("salad"));
        assertThat(insertedOrder.getItems().get(0).getProduct().getPrice(), is(new BigDecimal("3.56")));
        assertThat(insertedOrder.getItems().get(0).getQuantity(), is(2));
        assertThat(insertedOrder.getItems().get(0).getTaxedAmount(), is(new BigDecimal("7.84")));
        assertThat(insertedOrder.getItems().get(0).getTax(), is(new BigDecimal("0.72")));
        assertThat(insertedOrder.getItems().get(1).getProduct().getName(), is("tomato"));
        assertThat(insertedOrder.getItems().get(1).getProduct().getPrice(), is(new BigDecimal("4.65")));
        assertThat(insertedOrder.getItems().get(1).getQuantity(), is(3));
        assertThat(insertedOrder.getItems().get(1).getTaxedAmount(), is(new BigDecimal("15.36")));
        assertThat(insertedOrder.getItems().get(1).getTax(), is(new BigDecimal("1.41")));
    }

    @Test(expected = UnknownProductException.class)
    public void unknownProduct() {
        SellItemRequest unknownProductRequest = new SellItemRequest(1, "unknown product");
        List<SellItemRequest> requests = Collections.singletonList(unknownProductRequest);
        SellItemsRequest request = new SellItemsRequest(requests);
        request.getRequests().add(unknownProductRequest);

        useCase.run(request);
    }
}
