package com.fridge.fridgieApp.service;

import com.fridge.fridgieApp.dto.FridgeSummaryDto;
import com.fridge.fridgieApp.model.Fridge;
import com.fridge.fridgieApp.model.Product;

import java.time.LocalDate;
import java.util.List;


public interface FridgeService {
    Fridge addFridge(Fridge fridge);

    List<Fridge> getAllFridges();

    Fridge getFridgeById(long id);

    Fridge addProductToFridge(Long fridgeId, Product product);

    List<Product> getProductsExpiringInDateRange(Long fridgeId, LocalDate startDate, LocalDate endDate);

    List<Product> getProductsInFridge(long fridgeId, String sortBy, String order);

    List<Product> getExpiredProducts(Long fridgeId);

    List<Product> getLongestStoredProducts(Long fridgeId, int count);

    List<Product> getExpiringSoonProducts(long fridgeId, int daysBeforeExpiration);

    List<Product> searchProductsInFridge(long fridgeId, String name, String category);

    List<Product> getUseFirstSuggestions(long fridgeId, int count);

    List<Product> getWhatIsNew(long fridgeId, int days);

    FridgeSummaryDto getFridgeSummary(long fridgeId);
}
