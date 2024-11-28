package be.ucll.repositories;

import be.ucll.entities.Order;
import be.ucll.ui.SearchView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface OrderRepository {

	Collection<Order> findAll();
	@Query("SELECT o FROM Order o WHERE o.user.username = :email")
	List<Order> findByUserEmail(@Param("email") String email);
}
