package sample;

import javafx.beans.property.SimpleStringProperty;

public class Product {
    private int id;
    private SimpleStringProperty productName;
    private int quantity;
    private double price;

    public Product(int id, String productName, int quantity, double price) {
        this.id = id;
        this.productName = new SimpleStringProperty(productName);
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
