package com.fridge.fridgieApp.repository;

import com.fridge.fridgieApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
    Product findByExpirationDate(String expirationDate);
    Product findByDateAdded(String dateAdded);
    List<Product> findByFridgeId(long fridgeId);
    List<Product> findAll();
    void deleteById(long id);

    @Query("SELECT p FROM Product p WHERE p.expirationDate  BETWEEN  CURRENT DATE AND ?1")
    List<Product> findExpiringSoon(Long fridgeId,LocalDate soonToExpireDate);

    @Query("SELECT p FROM Product p WHERE p.fridge.id = ?1 AND p.expirationDate BETWEEN ?2 AND CURRENT DATE")
    List<Product> findByFridgeIdAndExpirationDateBetween(long fridgeId, LocalDate soonToExpireDate, LocalDate now);


}
