package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Project Inventory");
        primaryStage.setScene(new Scene(root, 450, 600));
        primaryStage.show();
        primaryStage.getIcons().add(new Image("file:data/guest-book.png"));
    }


    public static void main(String[] args) {
        launch(args);
    }

}
