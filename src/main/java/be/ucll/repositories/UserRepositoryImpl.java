package be.ucll.repositories;

import be.ucll.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Collection<User> findAll() {
        return entityManager.createQuery("from User").getResultList();
    }

    @Override
    public User findByUsername(String username) {

        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println("Fout bij het ophalen van de gebruiker: " + e.getMessage());
            return null;
        }
    }
}
