package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WelcomeView extends GridPane {
    // WELCOME
    public Button send;
    public TextField heightField;
    public TextField widthField;
    public TextField speciesField;
    public TextField foodField;
    public TextField obstacleField;
    final int[] params = new int[5];
    public static List<Scene> scenesList;
    List<SpeciesView> svList;
    Board b;

    public int intvalue (TextField tf){
        if(tf.getText().equals(""))
            return 0;
        return Integer.parseInt(tf.getText());
    }
    public float floatvalue (TextField tf){
        if(tf.getText().equals(""))
            return 0;
        return Float.parseFloat(tf.getText());
    }
    int minvalue (TextField tf){
        if( tf==heightField ){
            if(intvalue(widthField)==0) return 2;
            int i = (intvalue(obstacleField) + intvalue(widthField) -1)/intvalue(widthField);
            return Math.max(i, 2);
        }
        else if( tf==widthField ) {
            if(intvalue(heightField)==0) return 2;
            int i = (intvalue(foodField) + intvalue(obstacleField) + intvalue(heightField) - 1) / intvalue(heightField);
            return Math.max(i, 2);
        }
        else if (tf==(speciesField))
            return 1;
        else if (tf==(foodField))
            return 1;
        else return 0;
    }
    int maxvalue (TextField tf){
        int area = intvalue(heightField) * intvalue(widthField);
        if( tf==(heightField) || tf==(widthField) )
            return 100;
        else if ( tf==(foodField) )
            return 10;
        else if ( tf==(speciesField) )
            return 5;
        else if ( tf == obstacleField) return area;
        else for(int i=0; i<svList.size(); i++)
            {
                if(!tf.equals(svList.get(i).numberOfAnimals))
                {
                    area=area-intvalue(svList.get(i).numberOfAnimals);
                }
            }
        return area;
    }
    TextFormatter <Integer>  FormatterCreator(TextField tf){
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

        scenesList=new ArrayList<>();
        svList=new ArrayList<>();

        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        Label heightLabel = new Label("Height (between 2 and 100)");
        add(heightLabel, 0, 0);
        heightField = new TextField("20");
        add(heightField, 1, 0);

        Label widthLabel = new Label("Width (between 2 and 100)");
        add(widthLabel, 0, 1);
        widthField = new TextField("20");
        add(widthField, 1, 1);

        Label animalLabel = new Label("Number of species (1-5)");
        add(animalLabel, 0, 2);
        speciesField = new TextField("2");
        add(speciesField, 1, 2);

        Label obstacleLabel = new Label("Number of obstacles (>=0)");
        add(obstacleLabel, 0, 3);
        obstacleField = new TextField("20");
        add(obstacleField, 1, 3);

        Label foodLabel = new Label("food spawning frequency  (1-10)");
        add(foodLabel, 0, 4);
        foodField = new TextField("4");
        add(foodField, 1, 4);

        heightField.setTextFormatter(FormatterCreator(heightField));
        widthField.setTextFormatter(FormatterCreator(widthField));
        speciesField.setTextFormatter(FormatterCreator( speciesField ));
        obstacleField.setTextFormatter(FormatterCreator(obstacleField ));
        foodField.setTextFormatter(FormatterCreator(foodField ));


        send = new Button("set");
        add(send, 0, 5);
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Data validation
                if (!getParams(params)) {
                    Label label = new Label("Textfields values cannot be null! \n");
                    label.getStyleClass().add("labelWarning");
                    label.setTextFill(Color.RED);
                    add(label, 0, 6);
                } else {
                    b = new Board(params[0], params[1], params[2], params[3]);
                    int i=0;
                    while(params[4]-- > 0)
                    {
                        Species species=new Species();
                        b.speciesList.add(species);
                        SpeciesView sv=new SpeciesView(WelcomeView.this, species, i);
                        sv.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                        scenesList.add(new Scene( sv ));
                        svList.add(sv);
                        i++;
                    }
                    Main.primaryStage.setScene(scenesList.get(0));

                }
            }
        });
    }
    boolean getParams(final int[] params) {
        params[0] = intvalue(heightField);
        params[1] = intvalue(widthField);
        params[2] = intvalue(foodField);
        params[3] = intvalue(obstacleField);
        params[4] = intvalue(speciesField);
        return !heightField.getText().equals("") &&
                !widthField.getText().equals("") &&
                !speciesField.getText().equals("") &&
                !foodField.getText().equals("") &&
                !obstacleField.getText().equals("");
    }
    public void start()
    {
        for(Iterator<SpeciesView> it = svList.iterator(); it.hasNext();)
        {
            SpeciesView sv=it.next();
            sv.species.name=sv.nameField.getText();
            if(sv.fertilityField.getText()!="")
                sv.species.fertility=Float.parseFloat(sv.fertilityField.getText());
            if(sv.sightField.getText()!="")
                sv.species.sight=Float.parseFloat(sv.sightField.getText());
            if(sv.metabolismField.getText()!="")
                sv.species.metabolismSpeed=Float.parseFloat(sv.metabolismField.getText());
            b.generateAnimals(intvalue(sv.numberOfAnimals), sv.species);
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("boardview.fxml"));
            Parent root = loader.load();
            BoardView bv = loader.getController();
            bv.init(b);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();
        } catch(IOException e){
            System.out.println("failed");
        }
    }
}
