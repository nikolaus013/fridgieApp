package com.fridge.fridgieApp.controller;

import com.fridge.fridgieApp.model.Fridge;
import com.fridge.fridgieApp.model.Product;
import com.fridge.fridgieApp.service.FridgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/fridge")
@CrossOrigin(origins = "http://localhost:63342")
public class FridgeController {

    private final FridgeService fridgeService;
    @PostMapping("/addfridge")
    public ResponseEntity<Fridge>  addFridge(@Valid @RequestBody  Fridge fridge){
      return   ResponseEntity.status(HttpStatus.CREATED)
              .body(fridgeService.addFridge(fridge));
    }

    @PostMapping("/{fridgeId}/products")
    public ResponseEntity<Fridge> addProductToFridge(@Valid @RequestBody Product product, @PathVariable long fridgeId){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fridgeService.addProductToFridge(fridgeId, product));
    }

    @GetMapping("/{fridgeId}/expiring-soon")
    public ResponseEntity<List<Product>> getExpiringSoonProducts(@PathVariable long fridgeId, @RequestParam int daysBeforeExpiration){
        return ResponseEntity.status(HttpStatus.OK)
                .body(fridgeService.getExpiringProducts(fridgeId, daysBeforeExpiration));
    }

    @GetMapping("/{fridgeId}")
    public ResponseEntity<Fridge> getFridgeById(@PathVariable long fridgeId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(fridgeService.getFridgeById(fridgeId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Fridge>> getAllFridges(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(fridgeService.getAllFridges());
    }

    @GetMapping("/{fridgeId}/expire")
    public ResponseEntity<List<Product>> findExpiringProducts(@PathVariable long fridgeId,@RequestParam int startDate,@RequestParam int endDate){
        return ResponseEntity.status(HttpStatus.OK).body(fridgeService.getExpiringProducts(fridgeId, startDate));

    }



}
