package vn.edu.eaut.lab16.shared.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product entity representing items in the catalog.
 */
public class Product {
    private Long id;
    private String productCode;
    private String productName;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product() {
        this.available = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Product(String productCode, String productName, String description, BigDecimal price, int stockQuantity) {
        this();
        this.productCode = productCode;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isInStock() {
        return stockQuantity > 0 && available;
    }
}
