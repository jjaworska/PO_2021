package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;


public class WelcomeView extends GridPane {
    // WELCOME
    public Button send;
    public TextField heightField;
    public TextField widthField;
    public TextField animalField;
    public TextField foodField;
    public TextField obstacleField;

    int intvalue (TextField tf){
        if(tf.getText().equals(""))
            return 0;
        return Integer.parseInt(tf.getText());
    }
    int minvalue (TextField tf){
        if( tf==heightField ){
            if(intvalue(widthField)==0) return 2;
            int i = (intvalue(foodField) + intvalue(animalField) + intvalue(obstacleField) + intvalue(widthField) -1)/intvalue(widthField);
            return Math.max(i, 2);
        }
        else if( tf==widthField ) {
            if(intvalue(heightField)==0) return 2;
            int i = (intvalue(foodField) + intvalue(animalField) + intvalue(obstacleField) + intvalue(heightField) - 1) / intvalue(heightField);
            return Math.max(i, 2);
        }
        else if ( tf==(foodField) || tf==(animalField))
            return 1;
        else return 0;
    }
    int maxvalue (TextField tf){
        int area = intvalue(heightField) * intvalue(widthField);
        if( tf==(heightField) || tf==(widthField) )
            return 100;
        else if ( tf==(foodField) )
            return area - intvalue(obstacleField) - intvalue(animalField);
        else if ( tf==(animalField) )
            return area - intvalue(obstacleField) - intvalue(foodField);
        else return area - intvalue(animalField) - intvalue(foodField);
    }
    private TextFormatter <Integer>  FormatterCreator(TextField tf){
        return new TextFormatter<>(change -> {

            if (change.isDeleted()) {
                return change;
            }

            String txt = change.getControlNewText();

            if(txt.equals("")){
                return null;
            }
            if (txt.matches("0\\d+")) {
                return null;
            }
            if(!txt.matches("\\d+")) {
                return null;
            }

            try {
                int n = Integer.parseInt(txt);
                if(minvalue(tf)>n)
                    change.setText(String.valueOf(minvalue(tf)));
                else if(maxvalue(tf)<n)
                    return null;
                return change;
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }
    WelcomeView() {

        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        Label heightLabel = new Label("Height (between 2 and 100)");
        add(heightLabel, 0, 0);
        heightField = new TextField("10");
        add(heightField, 1, 0);

        Label widthLabel = new Label("Width (between 2 and 100)");
        add(widthLabel, 0, 1);
        widthField = new TextField("10");
        add(widthField, 1, 1);

        Label animalLabel = new Label("Number of animals (>0)");
        add(animalLabel, 0, 2);
        animalField = new TextField("1");
        add(animalField, 1, 2);

        Label obstacleLabel = new Label("Number of obstacles (>=0)");
        add(obstacleLabel, 0, 3);
        obstacleField = new TextField("0");
        add(obstacleField, 1, 3);

        Label foodLabel = new Label("Number of food portions (>0)");
        add(foodLabel, 0, 4);
        foodField = new TextField("1");
        add(foodField, 1, 4);

        heightField.setTextFormatter(FormatterCreator(heightField));
        widthField.setTextFormatter(FormatterCreator(widthField));
        animalField.setTextFormatter(FormatterCreator( animalField ));
        obstacleField.setTextFormatter(FormatterCreator(obstacleField ));
        foodField.setTextFormatter(FormatterCreator(foodField ));


        send = new Button("set");
        add(send, 0, 5);
    }
    boolean getParams(final int[] params) {
        params[0] = intvalue(heightField);
        params[1] = intvalue(widthField);
        params[2] = intvalue(animalField);
        params[3] = intvalue(foodField);
        params[4] = intvalue(obstacleField);
        return !heightField.getText().equals("") &&
                !widthField.getText().equals("") &&
                !animalField.getText().equals("") &&
                !foodField.getText().equals("") &&
                !obstacleField.getText().equals("");
    }
}
