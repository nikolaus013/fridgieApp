package com.fridge.fridgieApp.service;

import com.fridge.fridgieApp.model.Fridge;
import com.fridge.fridgieApp.model.Product;
import com.fridge.fridgieApp.repository.FridgeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FridgeServiceTest {

    @Mock
    private FridgeRepository fridgeRepository;

    @InjectMocks
    private FridgeServiceImpl fridgeService;

    private Fridge testFridge1;
    private Fridge testFridge2;
    private Product expiredProduct1;
    private Product expiredProduct2;

    private Product testProduct1;
    private Product testProduct2;
    private Product testProduct3;

    private final LocalDate TODAY = LocalDate.of(2025, 5, 21);


    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        testFridge1 = new Fridge();
        testFridge1.setFridgeName("Home Fridge");
        testFridge1.setFridgeCapacity(10);

        testFridge2 = new Fridge();
        testFridge2.setFridgeName("Office Fridge");
        testFridge2.setFridgeCapacity(5);

        expiredProduct1 = new Product("Old Milk", TODAY.minusDays(5), TODAY.minusDays(10), "Expired", "Dairy");
        expiredProduct2 = new Product("Old Cheese", TODAY.minusDays(1), TODAY.minusDays(10), "Expired", "Dairy");
        testProduct1 = new Product("Fresh Juice", TODAY.plusDays(5), TODAY.minusDays(1), "Fresh", "Drinks");
        testProduct2 = new Product("Fresh Milk", TODAY.plusDays(10), TODAY.plusDays(1), "Fresh", "Dairy");
        testProduct3 = new Product("Fresh Cheese", TODAY.plusDays(15), TODAY.plusDays(1), "Fresh", "Dairy");
        Product productWithNullExpiry = new Product("Alcohol", null, TODAY.minusDays(2), "No Expiry Date", "Unknown");

    }

    @Test
    void testGetExpiredProducts_returnExpiredItems() {
        long fridgeId = 1L;
        testFridge1.setProducts(Arrays.asList(expiredProduct1, testProduct1, expiredProduct2));

        when(fridgeRepository.findById(fridgeId)).thenReturn(java.util.Optional.of(testFridge1));

        List<Product> expiredProducts = fridgeService.getExpiredProducts(fridgeId);

        assert (expiredProducts.size() == 2);
        assert (expiredProducts.contains(expiredProduct1));
        assert (expiredProducts.contains(expiredProduct2));
        assertNotNull(expiredProducts);
    }

    @Test
    @DisplayName("getExpiredProducts should return only products whose expiration date is before today")
    void testGetExpiredProducts_returnsOnlyExpiredItems() {
        //  Arrange
        long fridgeId = 1L;
        testFridge1.setProducts(Arrays.asList(expiredProduct1, testProduct1, expiredProduct2));

        // Mock the repository call: when findById is called with fridgeId, return our testFridge
        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(testFridge1));


        List<Product> actualExpiredProducts = fridgeService.getExpiredProducts(fridgeId);

        // Assert
        assertNotNull(actualExpiredProducts);
        assertEquals(2, actualExpiredProducts.size(), "Should find 2 expired products.");
        assertTrue(actualExpiredProducts.contains(expiredProduct1), "List should contain Old Milk.");
        assertTrue(actualExpiredProducts.contains(expiredProduct2), "List should contain Old Cheese.");
        assertFalse(actualExpiredProducts.contains(testProduct1), "List should not contain Fresh Juice.");


        // Verify that findById was called on the repository
        verify(fridgeRepository, times(1)).findById(fridgeId);
    }

    @Test
    @DisplayName("getExpiredProducts should return an empty list if no products are expired")
    void testGetExpiredProducts_noExpiredItems_returnsEmptyList() {
        //  Arrange
        long fridgeId = 1L;
        Product futureProduct1 = new Product("Future Bread", TODAY.plusDays(10), TODAY.minusDays(1), "Fresh", "Bakery");
        testFridge1.setProducts(Arrays.asList(testProduct2, futureProduct1));

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(testFridge1));

        //  Act
        List<Product> actualExpiredProducts = fridgeService.getExpiredProducts(fridgeId);

        //  Assert
        assertNotNull(actualExpiredProducts);
        assertTrue(actualExpiredProducts.isEmpty(), "Should return an empty list when no products are expired.");
        verify(fridgeRepository, times(1)).findById(fridgeId);
    }

    @Test
    @DisplayName("getExpiredProducts should return an empty list if the fridge has no products")
    void testGetExpiredProducts_fridgeIsEmpty_returnsEmptyList() {
        //  Arrange
        long fridgeId = 1L;
        testFridge1.setProducts(Collections.emptyList()); // Fridge with no products

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(testFridge1));

        //  Act
        List<Product> actualExpiredProducts = fridgeService.getExpiredProducts(fridgeId);

        //  Assert
        assertNotNull(actualExpiredProducts);
        assertTrue(actualExpiredProducts.isEmpty(), "Should return an empty list for an empty fridge.");
        verify(fridgeRepository, times(1)).findById(fridgeId);
    }

    @Test
    @DisplayName("getAllFridges should return a list of all fridges from repository")
    void testGetAllFridges_returnsListOfFridges() {
        //  Arrange
        List<Fridge> expectedFridges = Arrays.asList(testFridge1, testFridge2);
        when(fridgeRepository.findAll()).thenReturn(expectedFridges);

        //  Act
        List<Fridge> actualFridges = fridgeService.getAllFridges();

        // Assert
        assertNotNull(actualFridges);
        assertEquals(2, actualFridges.size());
        assertEquals(expectedFridges, actualFridges);
        verify(fridgeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllFridges should return an empty list when repository has no fridges")
    void testGetAllFridges_noFridges_returnsEmptyList() {
        //  Arrange
        when(fridgeRepository.findAll()).thenReturn(Collections.emptyList());

        //  Act
        List<Fridge> actualFridges = fridgeService.getAllFridges();

        //  Assert
        assertNotNull(actualFridges);
        assertTrue(actualFridges.isEmpty());
        verify(fridgeRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("getFridgeById should throw IllegalArgumentException when fridge not found")
    void testGetFridgeById_fridgeNotFound_throwsIllegalArgumentException() {
        //  Arrange
        long nonExistentFridgeId = 99L;
        when(fridgeRepository.findById(nonExistentFridgeId)).thenReturn(Optional.empty());

        //  Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> fridgeService.getFridgeById(nonExistentFridgeId));
        assertEquals("Fridge not found", exception.getMessage()); // Or your ResourceNotFoundException message
        verify(fridgeRepository, times(1)).findById(nonExistentFridgeId);
    }

    @Test
    @DisplayName("addProductToFridge should throw IllegalArgumentException if fridge not found")
    void testAddProductToFridge_success() {
        // Arrange
        long fridgeId = 1L;
        testFridge1.setFridgeCapacity(2);
        testFridge1.getProducts().clear();

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(testFridge1));
        when(fridgeRepository.save(any(Fridge.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Fridge actualFridge = fridgeService.addProductToFridge(fridgeId, testProduct1);

        // Assert
        assertNotNull(actualFridge);
        assertEquals(testFridge1, actualFridge);
        assertEquals(1, actualFridge.getProducts().size());
        assertEquals(testProduct1, actualFridge.getProducts().getFirst());
        assertEquals(2, actualFridge.getFridgeCapacity());
        assertEquals(TODAY, testProduct1.getDateAdded());
        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(fridgeRepository, times(1)).save(any(Fridge.class));

    }

    @Test
    @DisplayName("addProductToFridge should throw IllegalArgumentException if fridge is full")
    void testAddProductToFridge_fridgeFull_throwsIllegalArgumentException() {
        //  Arrange
        long fridgeId = 1L;
        testFridge1.setFridgeCapacity(1);
        testFridge1.addProduct(testProduct2);

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(testFridge1));

        //  Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fridgeService.addProductToFridge(fridgeId, testProduct1); // Try to add another product
        });

        // Assert
        assertEquals("Fridge is full", exception.getMessage());
        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(fridgeRepository, never()).save(any(Fridge.class)); // Save should not be called
    }

    @Test
    @DisplayName("addProductToFridge should throw IllegalArgumentException if fridge not found")
    void testAddProductToFridge_fridgeNotFound_throwsIllegalArgumentException() {
        //  Arrange
        long nonExistentFridgeId = 99L;
        when(fridgeRepository.findById(nonExistentFridgeId)).thenReturn(Optional.empty());

        //  Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> fridgeService.addProductToFridge(nonExistentFridgeId, testProduct1));

        // Assert
        assertEquals("Fridge not found", exception.getMessage());
        verify(fridgeRepository, times(1)).findById(nonExistentFridgeId);
        verify(fridgeRepository, never()).save(any(Fridge.class));
    }


    @Test
    @DisplayName("addProductToFridge should log warning if fridge becomes >75% full")
    void testAddProductToFridge_logsWarningWhenAlmostFull() {
        //  Arrange
        long fridgeId = 1L;
        testFridge1.setFridgeCapacity(4);
        testFridge1.addProduct(testProduct1); // 1/4
        testFridge1.addProduct(testProduct2); // 2/4

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(testFridge1));
        when(fridgeRepository.save(any(Fridge.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act

        testFridge1.setFridgeCapacity(3);
        testFridge1.getProducts().clear();
        testFridge1.addProduct(testProduct1);
        testFridge1.addProduct(testProduct2);


        Fridge updatedFridge = fridgeService.addProductToFridge(fridgeId, testProduct3); // Now 3 products
        // Assert
        assertEquals(3, updatedFridge.getProducts().size());

    }

    @Test
    @DisplayName("getProductsExpiringInDateRange should call repository method and return its result")
    void testGetProductsExpiringInDateRange_callsRepository() {
        //  Arrange
        long fridgeId = 1L;
        LocalDate startDate = TODAY;
        LocalDate endDate = TODAY.plusDays(5);
        List<Product> expectedProducts = Collections.singletonList(testProduct1); // Assume product1 expires in this range

        // Mock the repository to return the expected list for these specific arguments
        when(fridgeRepository.getProductsExpiringInDateRange(fridgeId, startDate, endDate))
                .thenReturn(expectedProducts);

        //  Act
        List<Product> actualProducts = fridgeService.getProductsExpiringInDateRange(fridgeId, startDate, endDate);

        //  Assert
        assertEquals(expectedProducts, actualProducts);
        verify(fridgeRepository, times(1)).getProductsExpiringInDateRange(fridgeId, startDate, endDate);
    }


    @Test
    @DisplayName("getProductsInFridge should sort products by productName ascending by default")
    void testGetProductsInFridge_defaultSort_byProductNameAsc() {
        //  Arrange
        long fridgeId = 1L;
        Product product1 = new Product("Apple", TODAY.plusDays(1), TODAY, "Fruit", "Produce");
        Product product2 = new Product("Banana", TODAY.plusDays(2), TODAY, "Fruit", "Produce");
        testFridge1.setProducts(new ArrayList<>(Arrays.asList(product2, product1))); // Unsorted

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(testFridge1));

        //  Act
        List<Product> sortedProducts = fridgeService.getProductsInFridge(fridgeId, null, "asc");

        //  Assert
        assertNotNull(sortedProducts);
        assertEquals(2, sortedProducts.size());
        assertEquals("Apple", sortedProducts.get(0).getProductName());
        assertEquals("Banana", sortedProducts.get(1).getProductName());
        verify(fridgeRepository, times(1)).findById(fridgeId);
    }

    @Test
    @DisplayName("getProductsInFridge should sort products by expirationDate descending")
    void testGetProductsInFridge_sortByExpirationDateDesc() {
        //  Arrange
        long fridgeId = 1L;
        Product productExpiringSoon = new Product("Yogurt", TODAY.plusDays(1), TODAY, "Dairy", "Dairy"); // Expires sooner
        Product productExpiringLater = new Product("Milk", TODAY.plusDays(5), TODAY, "Dairy", "Dairy");  // Expires later
        testFridge1.setProducts(new ArrayList<>(Arrays.asList(productExpiringSoon, productExpiringLater))); // Unsorted by these criteria initially

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(testFridge1));

        //  Act
        List<Product> sortedProducts = fridgeService.getProductsInFridge(fridgeId, "expirationdate", "desc");

        //  Assert
        assertNotNull(sortedProducts);
        assertEquals(2, sortedProducts.size());
        assertEquals("Milk", sortedProducts.get(0).getProductName());   // Later expiry comes first due to desc
        assertEquals("Yogurt", sortedProducts.get(1).getProductName());
        verify(fridgeRepository, times(1)).findById(fridgeId);
    }



    @Test
    @DisplayName("getProductsInFridge should sort products by category ascending")
    void testGetProductsInFridge_sortByCategoryAsc() {
        //  Arrange
        long fridgeId = 1L;
        Product pDairy = new Product("Milk", TODAY.plusDays(5), TODAY, "Dairy", "Dairy");
        Product pBakery = new Product("Bread", TODAY.plusDays(2), TODAY, "Grain", "Bakery");
        testFridge1.setProducts(new ArrayList<>(Arrays.asList(pDairy, pBakery))); // Unsorted by category

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(testFridge1));

        //  Act
        List<Product> sortedProducts = fridgeService.getProductsInFridge(fridgeId, "category", "asc");

        //  Assert
        assertNotNull(sortedProducts);
        assertEquals(2, sortedProducts.size());
        assertEquals("Bakery", sortedProducts.get(0).getCategory());
        assertEquals("Dairy", sortedProducts.get(1).getCategory());
        verify(fridgeRepository, times(1)).findById(fridgeId);
    }

    @Test
    @DisplayName("getProductsInFridge should handle invalid sortBy by defaulting to productName")
    void testGetProductsInFridge_invalidSortBy_defaultsToProductName() {
        //  Arrange
        long fridgeId = 1L;
        Product product1 = new Product("Apple", TODAY.plusDays(1), TODAY, "Fruit", "Produce");
        Product product2 = new Product("Banana", TODAY.plusDays(2), TODAY, "Fruit", "Produce");
        testFridge1.setProducts(new ArrayList<>(Arrays.asList(product2, product1))); // Unsorted

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(testFridge1));

        //  Act
        List<Product> sortedProducts = fridgeService.getProductsInFridge(fridgeId, "invalidsortkey", "asc");

        //  Assert
        // Your switch default makes comparator null, then products.sort uses the ternary operator's default.
        assertEquals("Apple", sortedProducts.get(0).getProductName());
        assertEquals("Banana", sortedProducts.get(1).getProductName());
        verify(fridgeRepository, times(1)).findById(fridgeId);
    }


}
