package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MainWindow {
    @FXML
    private Label welcomeLabel;
    @FXML
    private TableView<Product> tableView;
    @FXML
    private BorderPane borderPane;

    @FXML
    public void initialize(){
        welcomeLabel.setText(welcomeLabel.getText() + User.getSavedUser().getUsername());
        refresh();
    }

    @FXML
    public void refresh() {
        Connection connection = DataBaseConnection.getConnection();
        if(connection != null) {
            try {
                String command = "SELECT * FROM " + User.getSavedUser().getUsername() + "_products";
                PreparedStatement statement = connection.prepareStatement(command);
                ResultSet resultSet = statement.executeQuery();
                ObservableList<Product> products = FXCollections.observableArrayList();
                int idCount = 1;
                while (resultSet.next()){
                    products.add(new Product(idCount, resultSet.getString(2), resultSet.getInt(3), resultSet.getDouble(4)));
                    idCount++;
                }
                tableView.setItems(products);
                try {
                    tableView.getSelectionModel().selectFirst();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void addItem() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        dialog.setTitle("Add new product");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("productDialogAdd.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Connection connection = DataBaseConnection.getConnection();
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() || result.get().equals(ButtonType.OK)){
            ProductDialogAdd productDialog = fxmlLoader.getController();
            Product product = productDialog.processResult();
            try {
                String add = "INSERT INTO " + User.getSavedUser().getUsername() + "_products" + "(productName, quantity, price) values ( ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(add);
                preparedStatement.setString(1, product.getProductName());
                preparedStatement.setInt(2, product.getQuantity());
                preparedStatement.setDouble(3, product.getPrice());
                preparedStatement.executeUpdate();
                refresh();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void editItem() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        String productName = selectedProduct.getProductName();
        dialog.setTitle("Edit the product");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("productDialogEdit.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProductDialogEdit productDialogEdit = fxmlLoader.getController();
        productDialogEdit.setProduct(selectedProduct);
        productDialogEdit.initialize();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Connection connection = DataBaseConnection.getConnection();
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() || result.get().equals(ButtonType.OK)){
            ProductDialogEdit productDialog = fxmlLoader.getController();
            Product product = productDialog.processResult();
            try {
                String update = "UPDATE " + User.getSavedUser().getUsername() + "_products SET productName=?, quantity=?, price=? WHERE productName=?" ;
                PreparedStatement preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, product.getProductName());
                preparedStatement.setInt(2, product.getQuantity());
                preparedStatement.setDouble(3, product.getPrice());
                preparedStatement.setString(4, productName);
                preparedStatement.executeUpdate();
                refresh();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void deleteItem() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete product");
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        alert.setHeaderText("Delete product " + selectedProduct.getProductName());
        alert.setContentText("Are you sure?");

        Connection connection = DataBaseConnection.getConnection();
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get().equals(ButtonType.OK)){
            try {
                String delete = "DELETE FROM " + User.getSavedUser().getUsername() + "_products WHERE productName=?";
                PreparedStatement preparedStatement = connection.prepareStatement(delete);
                preparedStatement.setString(1, selectedProduct.getProductName());
                preparedStatement.executeUpdate();
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
