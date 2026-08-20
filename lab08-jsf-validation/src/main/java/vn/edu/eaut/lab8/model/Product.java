package vn.edu.eaut.lab8.model;

import java.io.Serializable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Mã sản phẩm không được để trống!")
    private String id;

    @NotBlank(message = "Tên sản phẩm không được để trống!")
    @Size(min = 2, max = 100, message = "Tên sản phẩm phải từ 2 đến 100 ký tự!")
    private String name;

    @NotNull(message = "Giá sản phẩm không được để trống!")
    @DecimalMin(value = "0.01", message = "Giá sản phẩm phải lớn hơn 0!")
    private Double price;

    @NotNull(message = "Số lượng không được để trống!")
    @Min(value = 0, message = "Số lượng sản phẩm phải lớn hơn hoặc bằng 0!")
    private Integer quantity;

    @NotBlank(message = "Vui lòng chọn danh mục sản phẩm!")
    private String category;

    public Product() {
    }

    public Product(String id, String name, Double price, Integer quantity, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
