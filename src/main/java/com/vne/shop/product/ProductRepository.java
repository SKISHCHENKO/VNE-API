package com.vne.shop.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryIgnoreCaseContaining(String category);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategoryIgnoreCaseContainingAndNameContainingIgnoreCase(String category, String name);
}
