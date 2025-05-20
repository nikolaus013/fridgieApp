package com.fridge.fridgieApp.service;

import com.fridge.fridgieApp.model.Fridge;
import com.fridge.fridgieApp.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;


public interface FridgeService {
    public Fridge addFridge(Fridge fridge);

    public List<Fridge> getAllFridges();

    public Fridge getFridgeById(long id);

    public Fridge addProductToFridge(Long fridgeId, Product product);
    public List<Product> getExpiringProducts(Long fridgeId, int daysBeforeExpiration);
}
