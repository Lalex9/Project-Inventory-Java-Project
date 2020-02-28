package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ProductDialogEdit {
    @FXML
    private TextField productField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceField;

    private Product product;

    public void setProduct(Product product) {
        this.product = product;
    }

    @FXML
    public void initialize() {
        if (product != null) {
            productField.setText(product.getProductName());
            quantityField.setText(product.getQuantity()+"");
            priceField.setText(product.getPrice()+"");
        }
    }

    @FXML
    public Product processResult() {
        String productName = productField.getText().trim();
        int quanity = Integer.parseInt(quantityField.getText().trim());
        double price = Double.parseDouble(priceField.getText().trim());
        Product product = new Product(0, productName, quanity, price);
        return product;
    }
}
