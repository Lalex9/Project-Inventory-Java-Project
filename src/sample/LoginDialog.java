package sample;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginDialog {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    public User processResult() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        User user = new User(username,password);
        return user;
    }
}
