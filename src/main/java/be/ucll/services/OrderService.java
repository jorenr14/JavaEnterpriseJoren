package be.ucll.services;

import be.ucll.entities.Order;

import java.util.Collection;

public interface OrderService {
    Collection<Order> findAll();
}
