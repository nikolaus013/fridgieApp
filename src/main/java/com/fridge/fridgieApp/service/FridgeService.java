package com.fridge.fridgieApp.service;

import com.fridge.fridgieApp.model.Fridge;
import com.fridge.fridgieApp.model.Product;
import java.util.List;


public interface FridgeService {
     Fridge addFridge(Fridge fridge);

     List<Fridge> getAllFridges();

     Fridge getFridgeById(long id);

     Fridge addProductToFridge(Long fridgeId, Product product);
     List<Product> getExpiringProducts(Long fridgeId, int daysBeforeExpiration);
}
