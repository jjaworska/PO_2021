package sample;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class Main extends Application {
    public static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Environment");

        // WELCOME VIEW
        WelcomeView welcome = new WelcomeView();
        Scene welcomeScene = new Scene(welcome);
        welcomeScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        this.primaryStage.setScene(welcomeScene);

        primaryStage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
