package be.ucll.repositories;

import java.util.Collection;

import be.ucll.entities.Product;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Collection<Product> findAll() {
		return entityManager.createQuery("from Product").getResultList();
	}
}
