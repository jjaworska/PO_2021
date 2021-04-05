package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Environment");
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        board b=new board(15, 20, 10, 10, 20);
        BoardView bv=new BoardView(b);
        Scene scene = new Scene(bv, b.height*20, b.width*20);
        primaryStage.setScene(scene);
        primaryStage.show();
        Simulator simulator = new Simulator (bv);
        simulator.start();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
