package com.fridge.fridgieApp.model;

import com.fridge.fridgieApp.model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Fridge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fridgeName;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int fridgeCapacity;

    @ElementCollection
    private List<Product> products = new ArrayList<>();

    // Helper method to add products
    public void addProduct(Product product) {
        if (products.size() >= fridgeCapacity) {
            throw new IllegalStateException("Fridge is full");
        }
        product.setDateAdded(LocalDate.now());
        products.add(product);
    }
}