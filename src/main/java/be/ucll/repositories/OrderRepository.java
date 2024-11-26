package be.ucll.repositories;

import be.ucll.entities.Order;
import be.ucll.ui.SearchView;

import java.util.Collection;

public interface OrderRepository {

	Collection<Order> findAll();
}
