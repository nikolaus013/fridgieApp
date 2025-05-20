package com.fridge.fridgieApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {


    private long id;
    private String productName;
    private LocalDate expirationDate;
    private LocalDate dateAdded;
    private String description;
    private String category;


}
