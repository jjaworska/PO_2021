package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class WelcomeView extends GridPane {
    // WELCOME
    public Button send;
    public TextField heightField;
    public TextField widthField;
    public TextField animalField;
    public TextField foodField;
    public TextField obstacleField;
    WelcomeView() {
        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        Label heightLabel = new Label("Height (between 10 and 30)");
        add(heightLabel, 0, 0);
        heightField = new TextField("20");
        add(heightField, 1, 0);

        Label widthLabel = new Label("Width (between 10 and 30)");
        add(widthLabel, 0, 1);
        widthField = new TextField("20");
        add(widthField, 1, 1);

        Label animalLabel = new Label("Number of animals (>0)");
        add(animalLabel, 0, 2);
        animalField = new TextField("10");
        add(animalField, 1, 2);

        Label obstacleLabel = new Label("Number of obstacles (>=0)");
        add(obstacleLabel, 0, 3);
        obstacleField = new TextField("15");
        add(obstacleField, 1, 3);

        Label foodLabel = new Label("Number of food portions (>0)");
        add(foodLabel, 0, 4);
        foodField = new TextField("10");
        add(foodField, 1, 4);

        send = new Button("set");
        add(send, 0, 5);
    }
    boolean getParams(final int[] params) {
        params[0] = Integer.parseInt(widthField.getText());
        params[1] = Integer.parseInt(heightField.getText());
        params[2] = Integer.parseInt(animalField.getText());
        params[3] = Integer.parseInt(obstacleField.getText());
        params[4] = Integer.parseInt(foodField.getText());
        for (int i = 0; i < 5; i++)
            if ((i != 3 && params[i] <= 0) || params[i] < 0)
                return false;
        for (int i = 0; i < 2; i++)
            if (params[i] < 5 || params[i] > 30)
                return false;
        if (1.5 * (params[2] + params[3] + params[4]) > params[0] * params[1])
            return false;
        return true;
    }
}
