package be.ucll.services;

import be.ucll.entities.Order;
import be.ucll.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final List<Order> orders;
    public OrderServiceImpl() {
        this.orders = new ArrayList<>();
        // Voeg mockdata toe
        this.orders.add(new Order(1, "12345", 3, true));
        this.orders.add(new Order(2, "67890", 5, false));
        this.orders.add(new Order(3, "54321", 2, true));


    }


    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Collection<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findOrders(String productName, Double minAmount, Double maxAmount, Boolean delivered, String email) {
        return orderRepository.findAll().stream().filter(order -> {
                    boolean matches = true;

                    if (productName != null && !productName.isEmpty()) {
                        matches &= order.getProducts().stream()
                                .anyMatch(product -> product.getName().contains(productName));
                    }

                    if (minAmount != null) {
                        matches &= order.getTotalAmount() >= minAmount;
                    }

                    if (maxAmount != null) {
                        matches &= order.getTotalAmount() <= maxAmount;
                    }

                    if (delivered != null) {
                        matches &= order.isDelivered()==delivered;
                    }

                    return matches;
                })
                .toList();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orders.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElse(null);
    }


}
