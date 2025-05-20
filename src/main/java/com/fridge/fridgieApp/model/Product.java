package com.fridge.fridgieApp.model;

import jakarta.persistence.Embeddable;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
    private String productName;
    @FutureOrPresent(message = "Expiration date must be in the future")
    private LocalDate expirationDate;
    private LocalDate dateAdded;
    private String description;
    private String category;
}
