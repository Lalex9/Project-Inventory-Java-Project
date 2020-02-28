package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class Controller {
    @FXML
    private Label connectionStatus;
    @FXML
    private Button connectButton;
    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;
    @FXML
    private GridPane startPane;

    private Connection connection;

    @FXML
    public void initialize(){
        connectionStatus.setText("Not Connected");
    }

    @FXML
    public void connectToDb() {
        connectionStatus.setText("Connecting to Database...");
        Runnable task = new Runnable() {
            @Override
            public void run() {
                DataBaseConnection.initializeConnection();
                connection = DataBaseConnection.getConnection();
                String status;
                if (connection != null) {
                    status = "Connected";
                } else {
                    status = "Not Connected";
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        connectionStatus.setText(status);
                        if(status.equalsIgnoreCase("Connected")) {
                            registerButton.setDisable(false);
                            loginButton.setDisable(false);
                        }
                    }
                });
            }
        };
        new Thread(task).start();
    }

    @FXML
    public void showRegisterDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(startPane.getScene().getWindow());
        dialog.setTitle("Register");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("registerDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Could not load the dialog");
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get().equals(ButtonType.OK)) {
            RegisterDialog registerDialog = fxmlLoader.getController();
            User user = registerDialog.proccessResult();
            try {
                String check = "SELECT * FROM Users WHERE USERNAME=?";
                PreparedStatement statement = connection.prepareStatement(check);
                statement.setString(1, user.getUsername());
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Error");
                    alert.setContentText("The username " + user.getUsername() + " is already used.");
                    alert.showAndWait();
                } else {
                    String querry = "INSERT INTO Users (USERNAME, PASSWORD, PRODUCTS)" + "values ( ?, ?, ?)";
                    PreparedStatement preparedStatement =connection.prepareStatement(querry);
                    preparedStatement.setString(1, user.getUsername());
                    preparedStatement.setString(2, user.getPassword());
                    preparedStatement.setString(3, user.getUsername() + "_products");
                    preparedStatement.executeUpdate();
                    resultSet = statement.executeQuery();
                    if(resultSet.next()) {
                        String tableCreate = "CREATE TABLE IF NOT EXISTS "
                                            + user.getUsername() + "_products "
                                            + "(id INT(64) NOT NULL AUTO_INCREMENT, "
                                            + "productName VARCHAR(500), "
                                            + "quantity INT(64), "
                                            + "price DOUBLE(10,2), "
                                            + "PRIMARY KEY(id)) ";
                        PreparedStatement createStatement = connection.prepareStatement(tableCreate);
                        createStatement.executeUpdate();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Success");
                        alert.setContentText("Registration successful.");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Error");
                        alert.setHeaderText("Registration failed.");
                        alert.showAndWait();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void showLoginDialog(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(startPane.getScene().getWindow());
        dialog.setTitle("Login");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("loginDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Could not load the dialog");
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get().equals(ButtonType.OK)) {
            LoginDialog loginDialog = fxmlLoader.getController();
            User user = loginDialog.processResult();
            try {
                String check = "SELECT * FROM Users WHERE USERNAME=?";
                PreparedStatement statement = connection.prepareStatement(check);
                statement.setString(1, user.getUsername());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    check = "SELECT * FROM Users WHERE USERNAME=? AND PASSWORD=?";
                    statement = connection.prepareStatement(check);
                    statement.setString(1, user.getUsername());
                    statement.setString(2, user.getPassword());
                    resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Welcome");
                        alert.setContentText("Login successful.");
                        Optional<ButtonType> dialogResult = alert.showAndWait();
                        if (dialogResult.isPresent()) {
                            User.setSavedUser(user);
                            Stage stage = new Stage();
                            MainScreen newStage = new MainScreen();
                            newStage.start(stage);
                            System.out.println("Start application.");
                            Node node = (Node)actionEvent.getSource();
                            Stage stg = (Stage)node.getScene().getWindow();
                            stg.close();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Error");
                        alert.setContentText("The password for the username " + user.getUsername() + " is incorrect.");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Error");
                    alert.setContentText("The username " + user.getUsername() + " does not exist");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
