package com.fridge.fridgieApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fridge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fridgeName;
    private LocalDate productDateAdded;
    @Min(value = 1, message = "Capacity must be at least 1")
    private int fridgeCapacity;
    @ElementCollection
    @CollectionTable(name = "fridge_products", joinColumns = @JoinColumn(name = "fridge_id"))
    private List<Product> products = new ArrayList<>();


    public void addProduct(Product product){
        if(products.size() < fridgeCapacity){
            products.add(product);
        }else {
            throw new IllegalArgumentException("Fridge is full");
        }
        product.setDateAdded(LocalDate.now());
        products.add(product);
    }

}
