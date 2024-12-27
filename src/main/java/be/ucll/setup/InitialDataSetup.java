package be.ucll.setup;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

            // Maak gebruiker
            User user1 = new User("test", "test", "test@example.com");
            entityManager.persist(user1);

            // Maak eerste order
            Order order1 = new Order("John Doe", LocalDate.now(), 2, 120.50, true, user1);
            entityManager.persist(order1);

            // Maak tweede order
            Order order2 = new Order("Jane Doe", LocalDate.now(), 1, 75.99, false, user1);
            entityManager.persist(order2);

            // Voeg producten toe aan eerste order
            Product product1 = new Product("Laptop", 999.99, order1);
            Product product2 = new Product("Muis", 25.99, order1);
            entityManager.persist(product1);
            entityManager.persist(product2);

            // Voeg product toe aan tweede order
            Product product3 = new Product("Toetsenbord", 49.99, order2);
            entityManager.persist(product3);


            System.out.println("Order toegevoegd: " + order1.getCustomerName() + ", Totaal: " + order1.getTotalAmount());


            return null;
		});
	}
}