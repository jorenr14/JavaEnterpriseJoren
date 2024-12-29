package be.ucll.services;

import be.ucll.entities.Order;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    Collection<Order> findAll();

    List<Order> findOrders(String productName, Double minAmount, Double maxAmount, Boolean delivered, String email);

    Optional<Order> getOrderById(Long orderId);

    List<String> findProductNames(String productName);

}
