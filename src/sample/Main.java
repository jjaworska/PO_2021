package sample;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Environment");

        // WELCOME VIEW
        final int[] params = new int[5];
        WelcomeView welcome = new WelcomeView();
        Scene welcomeScene = new Scene(welcome);

        welcome.send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Data validation
                if(!welcome.getParams(params)) {
                    Label label = new Label("Textfields values cannot be null! \n");
                    label.setTextFill(Color.RED);
                    welcome.add(label, 0, 6);
                }
                else  {
                    board b = new board(params[0], params[1], params[2], params[3], params[4]);
                    BoardView bv = new BoardView(b);
                    Scene scene = new Scene(bv);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                    Simulator simulator = new Simulator(bv);
                }
            }
        });

        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
