package sample;

import javafx.animation.Animation;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WelcomeView extends VBox {
    // WELCOME
    public Label title;
    GridPane pane;
    public TextField heightField;
    public TextField widthField;
    public TextField speciesField;
    public TextField foodField;
    public TextField obstacleField;
    public Button send;
    final int[] params = new int[5];
    public static List<Scene> scenesList;
    List<SpeciesView> svList;
    Board b;

    public int intvalue (TextField tf){
        if(tf.getText().equals(""))
            return 0;
        return Integer.parseInt(tf.getText());
    }
    int minvalue (TextField tf){
        if( tf==heightField ){
            if(intvalue(widthField)==0) return 4;
            int i = (2 * intvalue(obstacleField))/intvalue(widthField) +1;
            return Math.max(i, 4);
        }
        else if( tf==widthField ) {
            if(intvalue(heightField)==0) return 4;
            int i = ( 2* intvalue(obstacleField) ) / intvalue(heightField) +1;
            return Math.max(i, 4);
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
        else if ( tf == obstacleField) return area/2-1;
        else {
            area=b.freeArea;
            for (SpeciesView speciesView : svList) {
                if (!tf.equals(speciesView.numberOfAnimals)) {
                    area = area - intvalue(speciesView.numberOfAnimals);
                }
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

        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(0, 0, 25, 0));

        Label heightLabel = new Label("Height (between 4 and 100)");
        pane.add(heightLabel, 0, 0);
        heightField = new TextField("20");
        pane.add(heightField, 1, 0);

        Label widthLabel = new Label("Width (between 4 and 100)");
        pane.add(widthLabel, 0, 1);
        widthField = new TextField("20");
        pane.add(widthField, 1, 1);

        Label animalLabel = new Label("Number of species (1-5)");
        pane.add(animalLabel, 0, 2);
        speciesField = new TextField("2");
        pane.add(speciesField, 1, 2);

        Label obstacleLabel = new Label("Number of obstacles (>=0)");
        pane.add(obstacleLabel, 0, 3);
        obstacleField = new TextField("20");
        pane.add(obstacleField, 1, 3);

        Label foodLabel = new Label("food spawning frequency  (1-10)");
        pane.add(foodLabel, 0, 4);
        foodField = new TextField("4");
        pane.add(foodField, 1, 4);

        heightField.setTextFormatter(FormatterCreator(heightField));
        widthField.setTextFormatter(FormatterCreator(widthField));
        speciesField.setTextFormatter(FormatterCreator( speciesField ));
        obstacleField.setTextFormatter(FormatterCreator(obstacleField ));
        foodField.setTextFormatter(FormatterCreator(foodField ));

        send = new Button("set");

        send.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Data validation
                if (!getParams(params)) {
                    Label label = new Label("Textfields values cannot be null! \n");
                    label.getStyleClass().add("labelWarning");
                    label.setTextFill(Color.RED);
                    pane.add(label, 0, 6);
                } else {
                    b = new Board(params[0], params[1], params[2], params[3], params[4]);
                    int i=0;
                    while(params[4]-- > 0)
                    {
                        Species species=new Species();
                        b.speciesList.add(species);
                        SpeciesView sv=new SpeciesView(WelcomeView.this, species, i);
                        sv.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
                        Scene toAdd = new Scene(sv);
                        scenesList.add(toAdd);
                        svList.add(sv);
                        i++;
                    }
                    Main.primaryStage.setScene(scenesList.get(0));

                }
            }
        });

        title = new Label("Choose board parameters");
        title.setPadding(new Insets(0, 0, 25, 0));
        title.getStyleClass().add("labelTitle");
        setAlignment(Pos.CENTER);
        setPadding(new Insets(25, 25, 25, 25));
        getChildren().addAll(title, pane, send);
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
        for (SpeciesView sv : svList) {
            sv.species.name = sv.nameField.getText();
            if (!sv.fertilityField.getText().equals(""))
                sv.species.geneSpeciesValue[Animal.FertilityId] = Float.parseFloat(sv.fertilityField.getText());
            if (!sv.sightField.getText().equals(""))
                sv.species.geneSpeciesValue[Animal.SightId] = Float.parseFloat(sv.sightField.getText());
            if (!sv.metabolismField.getText().equals(""))
                sv.species.geneSpeciesValue[Animal.MetabolismId] = Float.parseFloat(sv.metabolismField.getText());
            if (!sv.lifespanField.getText().equals(""))
                sv.species.geneSpeciesValue[Animal.LifespanId] = Float.parseFloat(sv.lifespanField.getText());
            if (!sv.mutationField.getText().equals(""))
                sv.species.mutationCoefficient = (Float.parseFloat(sv.mutationField.getText()) / 100);
            b.generateAnimals(intvalue(sv.numberOfAnimals), sv.species);
        }
        for(int i = 0; i < Animal.GENECOUNT; i++)
            b.starterGeneStats[i] /= b.starterAnimalCount;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("boardview.fxml"));
            Parent root = loader.load();
            BoardView bv = loader.getController();
            bv.init(b);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
            Main.primaryStage.setScene(scene);
            scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
                if(keyEvent.getCode().equals(KeyCode.P)) {
                    if (bv.timeline.getStatus().equals(Animation.Status.RUNNING))
                        bv.timeline.stop();
                    else
                        bv.timeline.play();
                }
            });
            Main.primaryStage.show();
        } catch(IOException e){
            System.out.println("failed");
        }
    }
}
