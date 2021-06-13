package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.Objects;


public class Main extends Application {
    public static Stage primaryStage;
    @Override
    public void start(Stage pS) {
        primaryStage = pS;
        primaryStage.setTitle("Environment");

        // WELCOME VIEW
        WelcomeView welcome = new WelcomeView();
        Scene welcomeScene = new Scene(welcome);
        welcomeScene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm()
        );
        primaryStage.setScene(welcomeScene);

        primaryStage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
