package it.gabrieletondi.telldontaskkata.order;

public interface OrderRepository {
    void save(Order order);

    Order getById(int orderId);
}
