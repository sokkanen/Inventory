package com.distsys.inventory.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public Product findByName(String name);
    public List<Product> findByNameContaining(String keyword);
}
