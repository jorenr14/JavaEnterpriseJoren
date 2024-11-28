package be.ucll.services;

import be.ucll.entities.Order;
import be.ucll.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
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
}
