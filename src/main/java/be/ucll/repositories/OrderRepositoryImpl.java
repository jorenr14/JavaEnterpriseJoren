package be.ucll.repositories;

import be.ucll.entities.Order;
import be.ucll.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
@Repository
public class OrderRepositoryImpl implements OrderRepository {

        @PersistenceContext
        private EntityManager entityManager;

        @Override
        public Collection<Order> findAll() {
            return entityManager.createQuery("from Order").getResultList();
        }


}
