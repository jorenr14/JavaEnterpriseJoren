package be.ucll.services;

import be.ucll.entities.Order;
import be.ucll.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Collection<Order> findAll() {
        return orderRepository.findAll();
    }
}
