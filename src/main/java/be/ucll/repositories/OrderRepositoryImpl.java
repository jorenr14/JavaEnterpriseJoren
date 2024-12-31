package be.ucll.repositories;

import be.ucll.entities.Order;
import be.ucll.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

        @PersistenceContext
        private EntityManager entityManager;

        @Override
        public Collection<Order> findAll() {
            return entityManager.createQuery("from Order").getResultList();
        }

        @Override
        public List<Order> findByUserEmail(String email) {
                return List.of();
        }

        @Override
        public Optional<Order> getOrderById(Long id) {
                return Optional.ofNullable(entityManager.find(Order.class, id));
        }

        @Override
        public List<String> findProductByName(String productname) {
                List<String> results = entityManager.createQuery(
                                "SELECT DISTINCT p.name FROM Product p WHERE LOWER(p.name) LIKE LOWER(:productname)", String.class)
                        .setParameter("productname", productname + "%") // Gebruik parameter voor LIKE-query
                        .getResultList();

                return results;
        }



}
