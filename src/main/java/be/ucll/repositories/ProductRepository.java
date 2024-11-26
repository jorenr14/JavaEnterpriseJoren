package be.ucll.repositories;

import be.ucll.entities.Product;

import java.util.Collection;

public interface ProductRepository {
    Collection<Product> findAll();
}
