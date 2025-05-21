package com.fridge.fridgieApp.service;

import com.fridge.fridgieApp.dto.FridgeSummaryDto;
import com.fridge.fridgieApp.dto.ProductSummaryDto;
import com.fridge.fridgieApp.model.Fridge;
import com.fridge.fridgieApp.model.Product;
import com.fridge.fridgieApp.repository.FridgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


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
            log.info("Fridge is almost full, please consider removing products, current percentage: {}", filledPercentage * 100);
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
    public List<Product> getLongestStoredProducts(Long fridgeId, int count) {
        Fridge fridge = fridgeRepository.findById(fridgeId).orElseThrow(() -> new IllegalArgumentException("Fridge not found"));
        return fridge.getProducts().stream().filter(product -> product.getExpirationDate()
                        .isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Product::getExpirationDate).reversed()).limit(count).toList();
    }

    @Override
    public List<Product> getExpiringSoonProducts(long fridgeId, int daysBeforeExpiration) {
        Fridge fridge = fridgeRepository.findById(fridgeId);
        log.info("Showing products expiring in {} days for fridge {}", daysBeforeExpiration, fridge.getFridgeName());
        return fridge.getProducts().stream()
                .filter(product -> product.getExpirationDate().isBefore(LocalDate.now().plusDays(daysBeforeExpiration)))
                .toList();

    }

    @Override
    public List<Product> searchProductsInFridge(long fridgeId, String name, String category) {
        Fridge fridge = fridgeRepository.findById(fridgeId);
        if (name != null && !name.trim().isEmpty()) {
            log.info("Searching for products with name {}", name);
            var lowerCaseName = name.toLowerCase();
            return fridge.getProducts().stream()
                    .filter(product -> product.getProductName().toLowerCase().contains(lowerCaseName))
                    .toList();
        } else if (category != null && !category.trim().isEmpty()) {
            log.info("Searching for products with category {}", category);
            var lowerCaseCategory = category.toLowerCase();
            return fridge.getProducts().stream()
                    .filter(product -> product.getCategory().toLowerCase().contains(lowerCaseCategory))
                    .toList();
        } else {
            log.info("Searching for all products in fridge {}", fridge.getFridgeName());
            return fridge.getProducts();
        }

    }

    @Override
    public List<Product> getUseFirstSuggestions(long fridgeId, int count) {
        Fridge fridge = fridgeRepository.findById(fridgeId);
        var allProducts = fridge.getProducts();

        Comparator<Product> useFirstComparator = Comparator.comparing(Product::getExpirationDate, Comparator.nullsFirst(LocalDate::compareTo))
                .thenComparing(Product::getDateAdded, Comparator.nullsFirst(LocalDate::compareTo));
        //Sorting
        log.info("Sorting products");
        allProducts.sort(useFirstComparator);
        log.info("Showing {} products", count);
        return allProducts.stream()
                .limit(count)
                .toList();
    }

    @Override
    public List<Product> getWhatIsNew(long fridgeId, int days) {
        Fridge fridge = fridgeRepository.findById(fridgeId);
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        log.info("Showing products added after {} for fridge {}", cutoffDate, fridgeId);
        return fridge.getProducts().stream().filter(product -> product.getDateAdded() != null && (product.getDateAdded().isEqual(cutoffDate) || product.getDateAdded().isAfter(cutoffDate)))
                .sorted(Comparator.comparing(Product::getDateAdded).reversed()) //Show the newest first
                .toList();

    }

    @Override
    public FridgeSummaryDto getFridgeSummary(long fridgeId) {
        Fridge fridge = fridgeRepository.findById(fridgeId);

        List<Product> fridgeProducts = fridge.getProducts();

        int daysForSoonToExpire = 7; // Or make this configurable
        LocalDate today = LocalDate.now();

        int totalProducts = fridgeProducts.size();
        long itemsExpiredCount = 0;
        long itemsExpiringSoonCount = 0;
        Set<String> categories = new HashSet<>();
        Product longestStoredProductRaw = null;

        if (!fridgeProducts.isEmpty()) {
            log.info("Calculating summary for fridge ID: {}, Name: {}", fridge.getId(), fridge.getFridgeName());

            itemsExpiredCount = fridgeProducts.stream()
                    .filter(product -> product.getExpirationDate() != null &&
                            product.getExpirationDate().isBefore(today))
                    .count();

            final LocalDate todayFinal = today; // effectively final for lambda
            final int daysForSoonToExpireFinal = daysForSoonToExpire;
            itemsExpiringSoonCount = fridgeProducts.stream()
                    .filter(product -> product.getExpirationDate() != null &&
                            !product.getExpirationDate().isBefore(todayFinal) && // Not already expired
                            product.getExpirationDate().isBefore(todayFinal.plusDays(daysForSoonToExpireFinal + 1)))
                    .count();

            fridgeProducts.forEach(product -> {
                if (product.getCategory() != null && !product.getCategory().trim().isEmpty()) {
                    categories.add(product.getCategory());
                }
            });

            // Finding the longest stored product
            Optional<Product> longestStoredProductOptional = fridgeProducts.stream()
                    .filter(p -> p.getDateAdded() != null)
                    .min(Comparator.comparing(Product::getDateAdded));

            if (longestStoredProductOptional.isPresent()) {
                longestStoredProductRaw = longestStoredProductOptional.get();
            }
        }

        ProductSummaryDto longestStoredProductDto = null;
        if (longestStoredProductRaw != null) {
            longestStoredProductDto = new ProductSummaryDto(
                    longestStoredProductRaw.getProductName(),
                    longestStoredProductRaw.getDateAdded(),
                    longestStoredProductRaw.getExpirationDate()
            );
        }


        return new FridgeSummaryDto(
                fridge.getId(),
                fridge.getFridgeName(),
                totalProducts,
                (int) itemsExpiringSoonCount,
                (int) itemsExpiredCount,
                longestStoredProductDto,
                categories,
                totalProducts, // currentCapacityUsed
                fridge.getFridgeCapacity()
        );
    }

}
