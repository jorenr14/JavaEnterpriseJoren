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
            User user1 = new User("test", "test", "reniers.joren@gmail.com");
            entityManager.persist(user1); // Persist gebruiker eerst

            // Maak eerste order
            Order order1 = new Order("John Doe", LocalDate.now(), 2, 65.98, true, user1);
            Order order2 = new Order("Jane Doe", LocalDate.now(), 1, 999.99, false, user1);

            // Voeg orders toe aan de gebruiker
            user1.setOrders(List.of(order1, order2));

            // Persist orders
            entityManager.persist(order1);
            entityManager.persist(order2);

            // Maak producten voor eerste order
            Product product1 = new Product("Muis", 25.99, order1);
            Product product2 = new Product("Toetsenbord", 49.99, order1);

            // Maak product voor tweede order
            Product product3 = new Product("Laptop", 999.99, order2);

            // Voeg producten toe aan orders
            order1.setProducts(List.of(product1, product2)); // Producten aan order koppelen
            order2.setProducts(List.of(product3));

            // Persist producten
            entityManager.persist(product1);
            entityManager.persist(product2);
            entityManager.persist(product3);

            // Debug output
            System.out.println("Gebruiker toegevoegd: " + user1.getUsername() + " - " + user1.getEmail());
            System.out.println("Order toegevoegd: " + order1.getCustomerName() + ", Totaal: " + order1.getTotalAmount());
            System.out.println("Order toegevoegd: " + order2.getCustomerName() + ", Totaal: " + order2.getTotalAmount());

            System.out.println("Producten voor Order 1: " + order1.getProducts());
            System.out.println("Producten voor Order 2: " + order2.getProducts());
            return null; // Commit transactie
        });
    }
}