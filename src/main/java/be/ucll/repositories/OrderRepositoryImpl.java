package be.ucll.repositories;

import be.ucll.entities.Order;
import be.ucll.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

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
        public List<String> findProductNamesContaining(String searchTerm) {
                return List.of();
        }


}
