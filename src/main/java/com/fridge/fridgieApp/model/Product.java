package com.fridge.fridgieApp.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String productName;
    private LocalDate expirationDate;
    private LocalDate dateAdded;
    private String description;
    private String category;
}
