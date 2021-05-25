package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;

public class SpeciesView extends GridPane {
    public static Board board;
    static int imagesCount = 6;
    int number;
    int image=0;
    public Button previous, next, previousimage, nextimage, ready;
    public TextField nameField, fertilityField, sightField, metabolismField, numberOfAnimals;
    public Species species;
    public Canvas canvas;
    WelcomeView wv;
    public void increment() {
        image=(image+1)%imagesCount;
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 100, 100);
        gc.drawImage(Images.t[image], 0, 0, 100, 100);
        species.images = Images.t[image];
        species.chartColor = Images.c[image];
    }
    public void decrement() {
        image=(image+imagesCount-1)%imagesCount;
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 100, 100);
        gc.drawImage(Images.t[image], 0, 0, 100, 100);
        species.images = Images.t[image];
        species.chartColor = Images.c[image];
        /*gc.drawImage(Images.t[image][Animal.UP], 0, 0, 100, 100);
        species.images=Images.t[image];*/
    }
    public SpeciesView(WelcomeView welcomeview, Species species, int i)
    {
        setAlignment(Pos.CENTER);
        wv=welcomeview;
        this.species=species;
        number=i;
        canvas=new Canvas(100, 100);
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        gc.drawImage(Images.t[image], 0, 0, 100, 100);
        species.images=Images.t[image];
        species.chartColor = Images.c[image];
        nameField=new TextField("Gatunek "+(number+1));
        fertilityField=new TextField("14");
        fertilityField.setTextFormatter(FloatFormatterCreator(fertilityField));
        sightField=new TextField("7");
        sightField.setTextFormatter(FloatFormatterCreator(sightField));
        metabolismField=new TextField("14");
        metabolismField.setTextFormatter(FloatFormatterCreator(metabolismField));
        numberOfAnimals=new TextField("10");
        numberOfAnimals.setTextFormatter(wv.FormatterCreator(numberOfAnimals));
        previous=new Button("prev");
        previous.setOnAction(actionEvent -> Main.primaryStage.setScene(WelcomeView.scenesList.get((number+WelcomeView.scenesList.size()-1)%WelcomeView.scenesList.size())));
        next=new Button("next");
        next.setOnAction(actionEvent -> Main.primaryStage.setScene(WelcomeView.scenesList.get((number+1)%WelcomeView.scenesList.size())));
        previousimage=new Button("prevIm");
        previousimage.setOnAction(actionEvent -> {
            decrement();
        });
        nextimage=new Button("nextIm");
        nextimage.setOnAction(actionEvent -> {
            increment();
        });
        ready=new Button("ready");
        ready.setOnAction(actionEvent -> wv.start());
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));
        add(previous, 0, 0);
        add(nameField, 1, 0);
        add(next, 2, 0);
        add(previousimage, 0, 2);
        add(canvas, 1, 2);
        add(nextimage, 2, 2);
        add(new Label("fertility:"), 1, 3);
        add(fertilityField, 1, 4);
        add(new Label("sight:"), 1, 5);
        add(sightField, 1, 6);
        add(new Label("metabolism speed:"), 1, 7);
        add(metabolismField, 1, 8);
        add(new Label("number of animals:"), 1, 9);
        add(numberOfAnimals, 1, 10);
        add(ready, 1, 11);
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
        return 1;
    }
    float maxvalue(TextField tf){
        return 20;
    }
}
