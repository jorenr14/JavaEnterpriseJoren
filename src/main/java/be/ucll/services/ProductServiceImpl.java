package be.ucll.services;


import be.ucll.entities.Product;
import be.ucll.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Collection<Product> findAll(){
        return productRepository.findAll();
    }
}
