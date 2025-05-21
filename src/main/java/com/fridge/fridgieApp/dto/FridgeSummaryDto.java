package com.fridge.fridgieApp.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FridgeSummaryDto {
    private long fridgeId;
    private String fridgeName;
    private int totalItems;
    private int itemsExpiringSoon;
    private int itemsExpired;
    private ProductSummaryDto longestStoredItem;
    private Set<String> categoriesPresent;
    private int currentCapacityUsed;
    private int maxCapacity;
}
