package com.fridge.fridgieApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryDto {

    private String productName;
    private LocalDate dateAdded;
    private LocalDate expirationDate;
}
