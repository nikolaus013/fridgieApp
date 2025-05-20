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


    @NotNull(message = "Name cannot be null")
    private String productName;

    @FutureOrPresent(message = "Must be in the future")
    private LocalDate expirationDate;
    private LocalDate dateAdded = LocalDate.now();
    private String description;
    private String category;


}
