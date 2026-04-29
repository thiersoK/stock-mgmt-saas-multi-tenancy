package com.thierso.sass_app.repositories;

import com.thierso.sass_app.entities.Category;
import com.thierso.sass_app.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findByReferenceIgnoreCase(String reference);
}
