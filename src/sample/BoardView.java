package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.text.DecimalFormat;


public class BoardView {
    DecimalFormat df = new DecimalFormat("#.##");
    static Image food_img = new Image(String.valueOf(BoardView.class.getResource("img/plant.png")));
    static Image obstacle_img = new Image(String.valueOf(BoardView.class.getResource("img/obstacle2.png")));
    static int M = 40;
    Board b;
    @FXML
    public Canvas canvas;
    @FXML
    public Button StartButton;
    @FXML
    public Button PauseButton;
    @FXML
    public Button StopButton;
    @FXML
    public VBox vbox;
    @FXML
    public Label fertilityLabel;
    @FXML
    public Label metabolismSpeedLabel;
    @FXML
    public Label sightLabel;

    private Timeline timeline;
    private boolean[] isSpeciesExtinct;
    public void init (Board b){
        this.b=b;
        this.timeline=new Timeline(new KeyFrame(Duration.millis(200), a->{
            if(b.makeStep())
                draw();
            else {
                drawEnd();
                timeline.stop();
            }
        }));
        this.canvas.setWidth(b.width*M);
        this.canvas.setHeight(b.height * M);
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        StartButton.setOnAction(actionEvent -> this.timeline.play());
        PauseButton.setOnAction(actionEvent -> this.timeline.stop());
        StopButton.setOnAction(actionEvent -> {
            this.timeline.stop();
            drawEnd();
        });

        int speciesNum = b.speciesList.size();
        isSpeciesExtinct = new boolean[speciesNum];
        for (boolean bool : isSpeciesExtinct)
            bool = false;
        int cnt = 0;
        for (Species s : b.speciesList) {
            vbox.getChildren().add(s.speciesName);
            s.speciesName.setText("Species " + s.name);
            vbox.getChildren().add(s.fertilitySpecies);
            vbox.getChildren().add(s.sightSpecies);
            vbox.getChildren().add(s.metabolismSpecies);
        }
    }

    public void draw()
    {
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        for(int i=0;i<b.height; i++)
        {
            for(int j=0; j<b.width; j++)
            {
                gc.setFill(Color.rgb(158,200,163));
                gc.fillRect(M*j, M*i, M, M);
                if(b.fields[i][j].animal != null) {
                    drawAnimal(b.fields[i][j].animal, M*j, M*i);
                    //gc.drawImage(animal_img[b.fields[i][j].animal.direction], M*j, M*i);
                }
                else if(b.fields[i][j].hasFood) {
                    gc.drawImage(food_img, M*j, M*i);
                }
                if(b.fields[i][j].obstacle) {
                    gc.drawImage(obstacle_img, M*j, M*i);
                }
            }
        }
        if(b.stepCount % 5 == 0) {
            sightLabel.setText("  sight: " + df.format(b.avgSight));
            fertilityLabel.setText("  fertility: " + df.format(b.avgFertility));
            metabolismSpeedLabel.setText("  metabolism speed: " + df.format(b.avgMetabolism));
            int cnt = 0;
            for (Species s : b.speciesList) {
                if(!s.isExtinct()) {
                    s.fertilitySpecies.setText("  fertility: " + df.format(s.sumFertility / s.animalList.size()));
                    s.sightSpecies.setText("  sight: " + df.format(s.sumSight / s.animalList.size()));
                    s.metabolismSpecies.setText("  metabolism speed: " + df.format(s.sumMetabolism / s.animalList.size()));
                }
                else if(!isSpeciesExtinct[cnt]) {
                    vbox.getChildren().remove(s.fertilitySpecies);
                    vbox.getChildren().remove(s.sightSpecies);
                    vbox.getChildren().remove(s.metabolismSpecies);
                    s.speciesName.setText("Species " + s.name + ": DEAD");
                    isSpeciesExtinct[cnt] = true;
                }
                cnt++;
            }
        }
    }

    public void drawAnimal(Animal a, int x, int y) {
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        if(a.isEgg())
            gc.fillOval(x, y, 15, 20);
        else
        {
            //gc.drawImage(a.species.images[a.direction], x, y);
            ImageView iv = new ImageView(a.species.images);
            iv.setRotate(a.direction*90);
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.rgb(158,200,163));
            gc.drawImage(iv.snapshot(params, null), x, y);
        }
    }

    public void drawEnd() {
        Label endgame = new Label("Simulation ended after " + this.b.stepCount + " steps \n");
        Label counter = new Label("Number of animals:\n"+b.starterAnimalCount+" --> "+b.animalCount);
        Label fertilityFinal = new Label("Evolution of fertility:\n"+df.format(b.starterFertility)+" --> "+df.format(b.avgFertility));
        Label metabolismSpeedFinal = new Label("Evolution of metabolism speed:\n"+df.format(b.starterMetabolism)+" --> "+df.format(b.avgMetabolism));
        Label sightFinal = new Label("Evolution of sight:\n"+df.format(b.starterSight)+" --> "+df.format(b.avgSight));
        counter.setTextAlignment(TextAlignment.CENTER);
        fertilityFinal.setTextAlignment(TextAlignment.CENTER);
        metabolismSpeedFinal.setTextAlignment(TextAlignment.CENTER);
        sightFinal.setTextAlignment(TextAlignment.CENTER);
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(20, 20, 20, 20));
        vb.getChildren().addAll(endgame, counter, fertilityFinal, metabolismSpeedFinal, sightFinal);
        Scene endView = new Scene(vb);
        endView.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        Main.primaryStage.setScene(endView);
    }
}
