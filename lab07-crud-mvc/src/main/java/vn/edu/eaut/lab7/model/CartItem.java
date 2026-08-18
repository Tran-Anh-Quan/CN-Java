package vn.edu.eaut.lab7.model;

public class CartItem {
    private SanPham product;
    private int quantity;

    public CartItem() {}

    public CartItem(SanPham product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public SanPham getProduct() { return product; }
    public void setProduct(SanPham product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
