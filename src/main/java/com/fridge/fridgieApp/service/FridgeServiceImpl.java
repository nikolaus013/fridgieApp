package com.fridge.fridgieApp.service;

import com.fridge.fridgieApp.model.Fridge;
import com.fridge.fridgieApp.model.Product;
import com.fridge.fridgieApp.repository.FridgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class FridgeServiceImpl implements FridgeService {
    final FridgeRepository fridgeRepository;


    @Override
    public Fridge addFridge(Fridge fridge) {
        return fridgeRepository.save(fridge);
    }


    @Override
    public List<Fridge> getAllFridges() {
        return fridgeRepository.findAll();
    }

    @Override
    public Fridge getFridgeById(long fridgeId) {
        return fridgeRepository.findById(fridgeId);
    }

    @Override
    public Fridge addProductToFridge(Long fridgeId, Product product) {
        Fridge fridge = fridgeRepository.findById(fridgeId).orElseThrow(() -> new IllegalArgumentException("Fridge not found"));
        if (fridge.getProducts().size() >= fridge.getFridgeCapacity()) {
            throw new IllegalArgumentException("Fridge is full");
        }
        var filledPercentage = (double) fridge.getProducts().size() / fridge.getFridgeCapacity();
        if (filledPercentage > 0.75) {
            log.info("Fridge is almost full, please consider removing products, current percentage: " + filledPercentage * 100);
        }
        fridge.addProduct(product);
        log.info("Product added to fridge: {}", product.getProductName());
        return fridgeRepository.save(fridge);
    }


    public List<Product> getProductsExpiringInDateRange(Long fridgeId, LocalDate startDate, LocalDate endDate) {
        return fridgeRepository.getProductsExpiringInDateRange(fridgeId, startDate, endDate);

    }

    @Override
    public List<Product> getProductsInFridge(long fridgeId, String sortBy, String order) {
        Fridge fridge = fridgeRepository.findById(fridgeId);
        var products = fridge.getProducts();
        Comparator<Product> comparator = null;

        if (sortBy != null) {
            comparator = switch (sortBy.toLowerCase()) {
                case "category" -> Comparator.comparing(Product::getCategory);
                case "expirationdate" -> Comparator.comparing(Product::getExpirationDate);
                case "productname" -> Comparator.comparing(Product::getProductName);
                default -> comparator;
            };
        }

        products.sort(comparator == null ? Comparator.comparing(Product::getProductName) : comparator);
        return products;
    }

    @Override
    public List<Product> getExpiredProducts(Long fridgeId) {
        Fridge fridge = fridgeRepository.findById(fridgeId).orElseThrow(() -> new IllegalArgumentException("Fridge not found"));

        return fridge.getProducts().stream()
                .filter(product -> product.getExpirationDate().isBefore(LocalDate.now()))
                .toList();
    }

    @Override
    public List<Product> getLongestStoredProducts(Long fridgeId,int count) {
        Fridge fridge = fridgeRepository.findById(fridgeId).orElseThrow(() -> new IllegalArgumentException("Fridge not found"));
       return fridge.getProducts().stream().filter(product -> product.getExpirationDate()
               .isAfter(LocalDate.now()))
               .sorted(Comparator.comparing(Product::getExpirationDate).reversed()).limit(count).toList();
    }

    @Override
    public List<Product> getExpiringSoonProducts(long fridgeId, int daysBeforeExpiration) {
        Fridge fridge = fridgeRepository.findById(fridgeId);

        return  fridge.getProducts().stream()
                .filter(product -> product.getExpirationDate().isBefore(LocalDate.now().plusDays(daysBeforeExpiration)))
                .toList();

    }

    @Override
    public List<Product> searchProductsInFridge(long fridgeId, String name,String category) {
        Fridge fridge = fridgeRepository.findById(fridgeId);
        if (name != null && name.trim().isBlank()) {
            var lowerCaseName = name.toLowerCase();
            return fridge.getProducts().stream()
                    .filter(product -> product.getProductName().toLowerCase().contains(lowerCaseName))
                    .toList();
        } else if (category != null && category.trim().isEmpty()) {
            var lowerCaseCategory = category.toLowerCase();
            return fridge.getProducts().stream()
                    .filter(product -> product.getCategory().toLowerCase().contains(lowerCaseCategory))
                    .toList();
        } else {
            return fridge.getProducts();
        }

    }

    @Override
    public List<Product> getUseFirstSuggestions(long fridgeId, int count) {
        return List.of();
    }

    @Override
    public List<Product> getWhatIsNew(long fridgeId, int days) {
        return List.of();
    }


    // TODO Implement  Summary/Dashboard information for fridge
}
