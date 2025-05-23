package com.fridge.fridgieApp.repository;

import com.fridge.fridgieApp.model.Fridge;
import com.fridge.fridgieApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FridgeRepository extends JpaRepository<Fridge, Long> {


    @Query("SELECT f.products FROM Fridge f WHERE f.id = :fridgeId AND " +
            "EXISTS (SELECT p FROM f.products p WHERE p.expirationDate BETWEEN :start AND :end)")
    List<Product> getProductsExpiringInDateRange(
            @Param("fridgeId") Long fridgeId,
            @Param("start") LocalDate startDate,
            @Param("end") LocalDate endDate
    );


}
