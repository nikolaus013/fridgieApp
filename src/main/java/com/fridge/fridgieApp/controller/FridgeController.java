package com.fridge.fridgieApp.controller;

import com.fridge.fridgieApp.model.Fridge;
import com.fridge.fridgieApp.model.Product;
import com.fridge.fridgieApp.service.FridgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/fridge")
public class FridgeController {

    private FridgeService fridgeService;
    @PostMapping("/addFridge")
    public ResponseEntity<Fridge>  addFridge(@RequestBody  Fridge fridge){
      return   ResponseEntity.status(HttpStatus.CREATED)
              .body(fridgeService.addFridge(fridge));
    }

    @PostMapping("/{fridgeId}/products")
    public ResponseEntity<Fridge> addProductToFridge(@RequestBody Product product, @PathVariable long fridgeId){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fridgeService.addProductToFridge(fridgeId, product));
    }

    @GetMapping("/{fridgeId}/expiring-soon")
    public ResponseEntity<List<Product>> getExpiringSoonProducts(@PathVariable long fridgeId, @RequestParam int daysBeforeExpiration){
        return ResponseEntity.status(HttpStatus.OK)
                .body(fridgeService.getExpiringProducts(fridgeId, daysBeforeExpiration));
    }


}
