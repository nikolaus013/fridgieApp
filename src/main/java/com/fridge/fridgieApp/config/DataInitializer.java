package com.fridge.fridgieApp.config;

import com.fridge.fridgieApp.model.Fridge;
import com.fridge.fridgieApp.model.Product;
import com.fridge.fridgieApp.repository.FridgeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner loadData(FridgeRepository fridgeRepository) {
        return args -> {
            if (fridgeRepository.count() == 0) {
                // Create 3 fridges
                Fridge fridge1 = createFridge("Home Fridge", 4);
                Fridge fridge2 = createFridge("Office Fridge", 2);
                Fridge fridge3 = createFridge("Garage Fridge", 6);

                // Add products to fridge1
                addProduct(fridge1, "Milk", LocalDate.now().plusDays(5), "Dairy");
                addProduct(fridge1, "Eggs", LocalDate.now().plusDays(10), "Protein");

                // Add products to fridge2
                addProduct(fridge2, "Soda", LocalDate.now().plusMonths(6), "Beverages");
                addProduct(fridge2, "Yogurt", LocalDate.now().plusDays(3), "Dairy");

                // Add products to fridge3
                addProduct(fridge3, "Beer", LocalDate.now().plusYears(1), "Beverages");
                addProduct(fridge3, "Cheese", LocalDate.now().plusDays(20), "Dairy");

                // Save all fridges
                fridgeRepository.saveAll(List.of(fridge1, fridge2, fridge3));
            }
        };
    }

    private Fridge createFridge(String name, int capacity) {
        Fridge fridge = new Fridge();
        fridge.setFridgeName(name);
        fridge.setFridgeCapacity(capacity);
        return fridge;
    }

    private void addProduct(Fridge fridge, String name, LocalDate expDate, String category) {
        Product product = new Product();
        product.setProductName(name);
        product.setExpirationDate(expDate);
        product.setCategory(category);
        fridge.addProduct(product);
    }
}
