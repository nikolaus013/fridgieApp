package com.fridge.fridgieApp.service;

import com.fridge.fridgieApp.model.Fridge;
import com.fridge.fridgieApp.model.Product;
import com.fridge.fridgieApp.repository.FridgeRepository;
import com.fridge.fridgieApp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

   private final ProductRepository productRepository;
   private final FridgeRepository fridgeRepository;

    @Override
    public Product getProductById(long id) {
        return null;
    }

    @Override
    public Product addProduct(Product product) {
        return null;
    }

    @Override
    public Product addProductToFridge(Product product, long fridgeId) {
        Fridge fridge = fridgeRepository.findById(fridgeId);
        if(fridge != null && fridge.getProducts().size() < fridge.getFridgeCapacity()){
            fridge.getProducts().add(product);

        }else {
            throw new IllegalArgumentException("Fridge is full");
        }

        var filledPercentage = (double)fridge.getProducts().size() / fridge.getFridgeCapacity();
        if(filledPercentage > 0.75){
            log.info("Fridge is almost full at {}%", filledPercentage * 100);
        }
        product.setFridge(fridge);
        return productRepository.save(product);



    }

    @Override
    public void deleteProduct(Product product) {
        productRepository.deleteById(product.getId());

    }

    @Override
    public void deleteProductById(long id) {

    }

    @Override
    public void deleteExpiredProducts() {

    }

    @Override
    public void deleteProductsOlderThanDays(LocalDate daysAgo) {

    }

    @Override
    public List<Product> findExpiringSoon(Long fridgeId, LocalDate soonToExpireDate) {
        LocalDate thresholdDate = LocalDate.now().plusDays(30);
        return productRepository.findExpiringSoon(fridgeId,thresholdDate);
    }

    @Override
    public List<Product> getExpiringSoonProducts(int days) {
        return List.of();
    }

    @Override
    public List<Product> getExpiringSoonProductsByFridgeId(Long fridgeId) {
       return List.of();

    }
}
