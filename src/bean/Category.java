package bean;

import java.util.List;

public class Category {
    private int id;
    private String name;
    private List<Product> Products;
    private List<List<Product>> ProductsByRow;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return Products;
    }

    public void setProducts(List<Product> products) {
        Products = products;
    }

    public List<List<Product>> getProductsByRow() {
        return ProductsByRow;
    }

    public void setProductsByRow(List<List<Product>> productsByRow) {
        ProductsByRow = productsByRow;
    }
}
