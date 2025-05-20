package com.fridge.fridgieApp.service;

import com.fridge.fridgieApp.model.Fridge;
import com.fridge.fridgieApp.model.Product;
import com.fridge.fridgieApp.repository.FridgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;



@Slf4j
@Service
@RequiredArgsConstructor
public class FridgeServiceImpl implements FridgeService{
    final FridgeRepository fridgeRepository;



    @Override
    public void addFridge(Fridge fridge) {
        fridgeRepository.save(fridge);
    }


    @Override
    public List<Fridge> getAllFridges() {
        return List.of();
    }

    @Override
    public Fridge getFridgeById(long id) {
        return null;
    }

    @Override
    public Fridge addProductToFridge(Long fridgeId, Product product) {
        Fridge fridge = fridgeRepository.findById(fridgeId).orElseThrow(() -> new IllegalArgumentException("Fridge not found"));
        if(fridge.getProducts().size() >= fridge.getFridgeCapacity()){
            throw new IllegalArgumentException("Fridge is full");
        }
        var filledPercentage = (double)fridge.getProducts().size() / fridge.getFridgeCapacity();
        if(filledPercentage > 0.75){
            log.info("Fridge is almost full, please consider removing products, current percentage: " + filledPercentage * 100);
        }
        fridge.addProduct(product);
        return fridgeRepository.save(fridge);
    }


    public List<Product> getExpiringProducts(Long fridgeId, int daysBeforeExpiration) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(daysBeforeExpiration);
        return fridgeRepository.findExpiringProducts(fridgeId, start, end);
    }
}
