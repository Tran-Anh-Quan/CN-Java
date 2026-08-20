package vn.edu.eaut.lab8.bean;

import vn.edu.eaut.lab8.model.Product;
import vn.edu.eaut.lab8.repository.ProductRepository;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "productBean")
@SessionScoped
public class ProductBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ProductRepository repository;
    private Product product;
    private List<Product> productList;
    private String searchKeyword;
    private boolean editMode;

    @PostConstruct
    public void init() {
        repository = new ProductRepository();
        product = new Product();
        editMode = false;
        loadData();
    }

    public void loadData() {
        productList = repository.search(searchKeyword);
    }

    public String prepareAdd() {
        product = new Product();
        editMode = false;
        return "product-form?faces-redirect=true";
    }

    public String prepareEdit(String id) {
        Product existing = repository.findById(id);
        if (existing != null) {
            product = new Product(existing.getId(), existing.getName(), existing.getPrice(), existing.getQuantity(), existing.getCategory());
            editMode = true;
            return "product-form?faces-redirect=true";
        }
        return null;
    }

    public String save() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!editMode && repository.findById(product.getId()) != null) {
            context.addMessage("productForm:id", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mã sản phẩm đã tồn tại!", null));
            return null;
        }

        repository.save(product);
        String msg = editMode ? "Cập nhật sản phẩm thành công!" : "Thêm mới sản phẩm thành công!";
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Thành công", msg));
        context.getExternalContext().getFlash().setKeepMessages(true);
        product = new Product();
        editMode = false;
        loadData();
        return "product-list?faces-redirect=true";
    }

    public String delete(String id) {
        repository.delete(id);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Thành công", "Đã xóa sản phẩm " + id));
        context.getExternalContext().getFlash().setKeepMessages(true);
        loadData();
        return "product-list?faces-redirect=true";
    }

    public String search() { loadData(); return null; }
    public String cancel() { product = new Product(); editMode = false; return "product-list?faces-redirect=true"; }

    // Getters & Setters
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public List<Product> getProductList() { return productList; }
    public void setProductList(List<Product> productList) { this.productList = productList; }

    public String getSearchKeyword() { return searchKeyword; }
    public void setSearchKeyword(String searchKeyword) { this.searchKeyword = searchKeyword; }

    public boolean isEditMode() { return editMode; }
    public boolean getEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }
}
