package be.ucll.repositories;

import be.ucll.entities.Order;
import be.ucll.ui.SearchView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

	Collection<Order> findAll();
	@Query("SELECT o FROM Order o WHERE o.user.username = :email")
	List<Order> findByUserEmail(@Param("email") String email);

	Optional<Order> getOrderById(Long id);

	@Query("SELECT DISTINCT p.name FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT(:searchTerm, '%'))")
	List<String> findProductByName(String productname);

}
