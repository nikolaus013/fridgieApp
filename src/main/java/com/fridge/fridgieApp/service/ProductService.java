package com.fridge.fridgieApp.service;

import com.fridge.fridgieApp.model.Product;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ProductService {

    public Product getProductById(long id);

    public Product addProduct(Product product);

    public Product addProductToFridge(Product product, long fridgeId);

    public void deleteProduct(Product product);
    public void deleteProductById(long id);

    public void deleteExpiredProducts();
    public void deleteProductsOlderThanDays(LocalDate daysAgo);
    public   List<Product> findExpiringSoon(Long fridgeId,LocalDate soonToExpireDate);
    public List<Product> getExpiringSoonProducts(int days);
    public List<Product> getExpiringSoonProductsByFridgeId(Long fridgeId);

}
