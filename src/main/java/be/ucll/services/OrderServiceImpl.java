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
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderRepository orderRepository;

    private final List<Order> orders;
    public OrderServiceImpl() {
        this.orders = new ArrayList<>();
    }

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
                        .anyMatch(product -> product.getName().equalsIgnoreCase(productName)); // Exacte match
            }


            if (minAmount != null) {
                matches &= order.getTotalAmount() >= minAmount;
            }


            if (maxAmount != null) {
                matches &= order.getTotalAmount() <= maxAmount;
            }

            if (delivered != null) {
                matches &= order.isDelivered() == delivered;
            }

            if (email != null && !email.isEmpty()) {
                matches &= order.getUser().getEmail().equalsIgnoreCase(email);
            }

            return matches;
        }).toList();
    }

    @Override
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.getOrderById(orderId);
    }

    @Override
    public List<String> findProductByName(String productName) {
        List<String> results = orderRepository.findProductByName(productName);
        return results;
    }


}
