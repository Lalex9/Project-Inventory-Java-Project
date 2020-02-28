package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ProductDialogAdd {
    @FXML
    private TextField productField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceField;

    @FXML
    public Product processResult() {
        String productName = productField.getText().trim();
        int quanity = Integer.parseInt(quantityField.getText().trim());
        double price = Double.parseDouble(priceField.getText().trim());
        Product product = new Product(0, productName, quanity, price);
        return product;
    }
}
