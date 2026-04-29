package com.thierso.sass_app.repositories;


import com.thierso.sass_app.entities.StockMvt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMvtRepository extends JpaRepository<StockMvt, String> {
}
