package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Nauczę się!");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new URL("file:///home/justyna/studia/OP/HelloFX/src/sample/sample.fxml"));
        VBox vbox = loader.<VBox>load();
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
