package vn.edu.eaut.lab16.shared.entity;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String productCode;
    private String productName;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private boolean available;

    public Product() {
        this.available = true;
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

    public boolean isInStock() {
        return stockQuantity > 0 && available;
    }
}
