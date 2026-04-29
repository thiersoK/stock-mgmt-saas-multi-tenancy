package com.thierso.sass_app.repositories;

import com.thierso.sass_app.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Optional<Category> findByNameIgnoreCase(String name);

}
