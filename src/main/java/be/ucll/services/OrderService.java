package be.ucll.services;

import be.ucll.entities.Order;

import java.util.Collection;
import java.util.List;

public interface OrderService {

    Collection<Order> findAll();

    List<Order> findOrders(String productName, Double minAmount, Double maxAmount, Boolean delivered, String email);

    Order getOrderById(Long orderId);

}
