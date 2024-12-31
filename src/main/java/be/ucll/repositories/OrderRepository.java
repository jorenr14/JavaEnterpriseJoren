package be.ucll.repositories;

import be.ucll.entities.Order;
import be.ucll.entities.User;
import be.ucll.ui.SearchView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

	Collection<Order> findAll();

	List<Order> findByUserEmail(@Param("email") String email);

	Optional<Order> getOrderById(Long id);


	List<String> findProductByName(String productname);


}
