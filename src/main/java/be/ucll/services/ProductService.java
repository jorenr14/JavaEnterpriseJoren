package be.ucll.services;

import be.ucll.entities.Product;

import java.util.Collection;

public interface ProductService {

    Collection<Product> findAll();
}
