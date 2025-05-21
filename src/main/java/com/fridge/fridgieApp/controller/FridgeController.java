package com.fridge.fridgieApp.controller;

import com.fridge.fridgieApp.dto.FridgeSummaryDto;
import com.fridge.fridgieApp.model.Fridge;
import com.fridge.fridgieApp.model.Product;
import com.fridge.fridgieApp.service.FridgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/fridge")
@CrossOrigin(origins = "http://localhost:63342")
public class FridgeController {

    private final FridgeService fridgeService;

    @PostMapping("/addfridge")
    public ResponseEntity<Fridge> addFridge(@Valid @RequestBody Fridge fridge) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fridgeService.addFridge(fridge));
    }

    @PostMapping("/{fridgeId}/products")
    public ResponseEntity<Fridge> addProductToFridge(@Valid @RequestBody Product product, @PathVariable long fridgeId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fridgeService.addProductToFridge(fridgeId, product));
    }

    @GetMapping("/{fridgeId}/expiring-soon")
    public ResponseEntity<List<Product>> getExpiringSoonProducts(@PathVariable long fridgeId, @RequestParam int daysBeforeExpiration){
        return ResponseEntity.status(HttpStatus.OK)
                .body(fridgeService.getExpiringSoonProducts(fridgeId,daysBeforeExpiration));
    }

    @GetMapping("/{fridgeId}")
    public ResponseEntity<Fridge> getFridgeById(@PathVariable long fridgeId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(fridgeService.getFridgeById(fridgeId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Fridge>> getAllFridges() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(fridgeService.getAllFridges());
    }

    @GetMapping("/{fridgeId}/products/expiring-in-range")
    public ResponseEntity<List<Product>> getProductsExpiringInDateRange(@PathVariable long fridgeId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(fridgeService.getProductsExpiringInDateRange(fridgeId, startDate, endDate));

    }


    @GetMapping("/{fridgeId}/fridgeproducts")
    public ResponseEntity<List<Product>> getProductsInFridge(@PathVariable long fridgeId, @RequestParam(required = false) String sortBy, @RequestParam(required = false, defaultValue = "asc") String order) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(fridgeService.getProductsInFridge(fridgeId, sortBy, order));
    }

    @GetMapping("/{fridgeId}/expired")
    public ResponseEntity<List<Product>> getExpiredProducts(@PathVariable long fridgeId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(fridgeService.getExpiredProducts(fridgeId));
    }

    @GetMapping("/{fridgeId}/longest-stored")
    public ResponseEntity<List<Product>> getLongestStoredProducts(@PathVariable long fridgeId, @RequestParam(defaultValue = "5") int count) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(fridgeService.getLongestStoredProducts(fridgeId, count));
    }

    @GetMapping("/{fridgeId}/products/search")
    public ResponseEntity<List<Product>> searchProductsInFridge(@PathVariable long fridgeId, @RequestParam(required = false) String name, @RequestParam(required = false) String category) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(fridgeService.searchProductsInFridge(fridgeId, name, category));
    }

    @GetMapping("/{fridgeId}/products/suggest-first")
    public ResponseEntity<List<Product>> useFirstSuggestion(@PathVariable long fridgeId,@RequestParam int count){
        return ResponseEntity.status(HttpStatus.OK).body(fridgeService.getUseFirstSuggestions(fridgeId,count));
    }


    @GetMapping("/{fridgeId}/products/show-newest")
    public ResponseEntity<List<Product>> getWhatIsNew(@PathVariable long fridgeId,@RequestParam int days){
        return ResponseEntity.status(HttpStatus.OK).body(fridgeService.getWhatIsNew(fridgeId,days));
    }

    @GetMapping("/{fridgeId}/summary")
    public ResponseEntity<FridgeSummaryDto> getFridgeSummary(@PathVariable long fridgeId){
        return ResponseEntity.status(HttpStatus.OK).body(fridgeService.getFridgeSummary(fridgeId));
    }


}
