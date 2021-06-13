package sample;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class SpeciesView extends GridPane {
    static int imagesCount = 6;
    int number;
    int image=0;
    public Button previous, next, previousImage, nextImage, ready;
    public TextField nameField, fertilityField, sightField, metabolismField, lifespanField, mutationField, numberOfAnimals;
    public CheckBox swimming, carrionFeeder;
    public Species species;
    public Canvas canvas;
    WelcomeView wv;
    public void drawImage() {
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 100, 100);
        gc.setFill(Color.web("FFFFFF88"));
        gc.fillRect(0,0, 100, 100);
        gc.drawImage(Images.t[image], 0, 0, 100, 100);
        species.speciesImage = Images.t[image];
        species.chartColor = Images.c[image];
    }
    public void increment() {
        image=(image+1)%imagesCount;
        drawImage();
    }
    public void decrement() {
        image=(image+imagesCount-1)%imagesCount;
        drawImage();
    }
    public SpeciesView(WelcomeView welcomeview, Species species, int i)
    {
        setAlignment(Pos.CENTER);
        wv=welcomeview;
        this.species=species;
        number=i;
        canvas=new Canvas(100, 100);
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        gc.setFill(Color.web("FFFFFF88"));
        gc.fillRect(0, 0, 100, 100);
        gc.drawImage(Images.t[image], 0, 0, 100, 100);
        species.speciesImage =Images.t[image];
        species.chartColor = Images.c[image];
        nameField=new TextField("Gatunek "+(number+1));
        fertilityField=new TextField("14");
        fertilityField.setTextFormatter(FloatFormatterCreator(fertilityField));
        sightField=new TextField("7");
        sightField.setTextFormatter(FloatFormatterCreator(sightField));
        metabolismField=new TextField("11");
        metabolismField.setTextFormatter(FloatFormatterCreator(metabolismField));
        lifespanField=new TextField("80");
        lifespanField.setTextFormatter(FloatFormatterCreator(lifespanField));
        numberOfAnimals=new TextField("10");
        numberOfAnimals.setTextFormatter(wv.FormatterCreator(numberOfAnimals));
        mutationField=new TextField("10");
        mutationField.setTextFormatter(wv.FormatterCreator(mutationField));
        previous=new Button("");
        GridPane.setHalignment(previous, HPos.RIGHT);
        previous.setOnAction(actionEvent -> Main.primaryStage.setScene(WelcomeView.scenesList.get((number+WelcomeView.scenesList.size()-1)%WelcomeView.scenesList.size())));
        next=new Button("");
        next.setOnAction(actionEvent -> Main.primaryStage.setScene(WelcomeView.scenesList.get((number+1)%WelcomeView.scenesList.size())));
        previousImage =new Button("");
        GridPane.setHalignment(previousImage, HPos.RIGHT);
        previousImage.setOnAction(actionEvent -> decrement());
        nextImage =new Button("");
        nextImage.setOnAction(actionEvent -> increment());
        previousImage.getStyleClass().add("buttonarrow");
        nextImage.getStyleClass().add("buttonarrow");
        previous.getStyleClass().add("buttonarrow");
        next.getStyleClass().add("buttonarrow");
        previousImage.setRotate(270);
        previous.setRotate(270);
        nextImage.setRotate(90);
        next.setRotate(90);
        ready=new Button("ready");
        ready.setOnAction(actionEvent -> wv.start());
        swimming= new CheckBox();
        swimming.selectedProperty().addListener((observable, oldValue, newValue) -> species.canSwim=newValue);
        carrionFeeder= new CheckBox();
        carrionFeeder.selectedProperty().addListener((observable, oldValue, newValue) -> species.carrionFeeder=newValue);
        setHgap(10);
        setVgap(10);
        setPrefWidth(600);
        setPadding(new Insets(25, 25, 25, 25));
        add(previous, 0, 0);
        add(nameField, 1, 0);
        add(next, 2, 0);
        add(previousImage, 0, 2);
        add(canvas, 1, 2);
        GridPane.setHalignment(canvas, HPos.CENTER);
        add(nextImage, 2, 2);
        add(new Label("fertility:"), 0, 3);
        add(fertilityField, 1, 3);
        add(new Label("sight:"), 0, 4);
        add(sightField, 1, 4);
        add(new Label("metabolism speed:"), 0, 5);
        add(metabolismField, 1, 5);
        add(new Label("minimum lifespan:"), 0, 6);
        add(lifespanField, 1, 6);
        add(new Label("number of animals:"), 0, 7);
        add(numberOfAnimals, 1, 7);
        add(new Label("mutation coefficient"), 0, 8);
        add(mutationField, 1, 8);
        add(new Label("Swimming:"), 0, 9);
        add(swimming, 1, 9);
        GridPane.setHalignment(swimming, HPos.CENTER);
        add(new Label("Carrion Feeder:"), 0, 10);
        add(carrionFeeder, 1, 10);
        GridPane.setHalignment(carrionFeeder, HPos.CENTER);
        add(ready, 1, 11);
        GridPane.setHalignment(ready, HPos.CENTER);
        setAlignment(Pos.CENTER);

        for(Node x : getChildren()) {
            if(x instanceof Label)
                GridPane.setHalignment(x, HPos.LEFT);
            else
                GridPane.setHalignment(x, HPos.CENTER);
        }

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(40);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(30);
        getColumnConstraints().addAll(col1,col2,col3);
    }

    TextFormatter<Float> FloatFormatterCreator(TextField tf){
        return new TextFormatter<>(change -> {

            if (change.isDeleted()) {
                return change;
            }

            String txt = change.getControlNewText();

            try {
                float f= Float.parseFloat(txt);
                if(minvalue(tf)>f)
                    change.setText(String.valueOf(minvalue(tf)));
                else if(maxvalue(tf)<f)
                    return null;
                return change;
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }
    float minvalue(TextField tf){
        if(tf == mutationField)
            return 0;
        return 1;
    }
    float maxvalue(TextField tf){
        if(tf == lifespanField)
            return 150;
        return 20;
    }
}
