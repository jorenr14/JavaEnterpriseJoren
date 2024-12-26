package be.ucll.setup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import be.ucll.entities.Order;
import be.ucll.entities.Product;
import be.ucll.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

//import be.ucll.repositories.TestEntity;

@Component
public class InitialDataSetup {

	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@PersistenceContext
	private EntityManager entityManager;

	@PostConstruct
	public void setup() {
		TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
		transactionTemplate.execute(e -> {

			User user1 = new User("test", "test");
            User user2 = new User("admin", "admin");
            entityManager.persist(user1);
            entityManager.persist(user2);

            // Voeg Producten toe
            Product product1 = new Product("Laptop", 799.99);
            Product product2 = new Product("Muis", 19.99);
            Product product3 = new Product("Toetsenbord", 49.99);
            entityManager.persist(product1);
            entityManager.persist(product2);
            entityManager.persist(product3);

            // Voeg Orders toe
            Order order1 = new Order(user1, List.of(product1, product2), new Date(), 819.98, true);
            Order order2 = new Order(user2, List.of(product3), new Date(), 49.99, false);
            entityManager.persist(order1);
            entityManager.persist(order2);

            return null;
		});
	}
}