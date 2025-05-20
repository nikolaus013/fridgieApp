package com.fridge.fridgieApp.repository;

import com.fridge.fridgieApp.model.Fridge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeRepository extends JpaRepository<Fridge, Long> {
    Fridge findByName(String name);
    Fridge findByCapacity(int capacity);
    Fridge findById(long id);

}
