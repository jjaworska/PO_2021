package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import javafx.util.Duration;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.ListIterator;


public class BoardView {
    static DecimalFormat df = new DecimalFormat("#.##");
    static Image food_img = new Image(String.valueOf(BoardView.class.getResource("img/plant.png")));
    static Image obstacle_img = new Image(String.valueOf(BoardView.class.getResource("img/obstacle2.png")));
    static Image dead_img = new Image(String.valueOf(BoardView.class.getResource("img/dead.png")));
    static Image tree_img = new Image(String.valueOf(BoardView.class.getResource("img/flower.png")));
    static int M = 40;
    static Board b;
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
        firstDraw();
    }

    public void firstDraw()
    {
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        for(int i=0;i<b.height; i++)
        {
            for(int j=0; j<b.width; j++)
            {
                gc.setFill(Color.YELLOWGREEN);
                if(b.fields[i][j].isWater) gc.setFill(Color.LIGHTBLUE);
                gc.fillRect(M*j, M*i, M, M);
                if(b.fields[i][j].obstacle) {
                    gc.drawImage(obstacle_img, M*j, M*i);
                }
                if(b.fields[i][j].tree){
                    gc.drawImage(tree_img, M*j, M*i);
                }
            }
        }
    }

    public void draw()
    {
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        for(int i=0;i<b.height; i++)
        {
            for(int j=0; j<b.width; j++)
            {
                gc.setFill(Color.YELLOWGREEN);
                if(b.fields[i][j].isWater) gc.setFill(Color.LIGHTBLUE);
                gc.fillRect(M*j, M*i, M, M);
                if(b.fields[i][j].animal != null) {
                    drawAnimal(b.fields[i][j].animal, j, i);
                }
                else if(b.fields[i][j].food) {
                    gc.drawImage(food_img, M*j, M*i);
                }
                if(b.fields[i][j].carrion>0){
                    gc.drawImage(dead_img, M*j, M*i);
                }
                if(b.fields[i][j].obstacle) {
                    gc.drawImage(obstacle_img, M*j, M*i);
                }
                if(b.fields[i][j].tree){
                    gc.drawImage(tree_img, M*j, M*i);
                }
            }
        }
        if(b.stepCount % 5 == 0) {
            sightLabel.setText("  sight: " + df.format(b.avgSight));
            fertilityLabel.setText("  fertility: " + df.format(b.avgFertility));
            metabolismSpeedLabel.setText("  metabolism speed: " + df.format(b.avgMetabolism));
            for (Species s : b.speciesList) {
                if(!s.isExtinct()) {
                    s.fertilitySpecies.setText("  fertility: " + df.format(s.sumFertility / s.animalList.size()));
                    s.sightSpecies.setText("  sight: " + df.format(s.sumSight / s.animalList.size()));
                    s.metabolismSpecies.setText("  metabolism speed: " + df.format(s.sumMetabolism / s.animalList.size()));
                }
                else if(!isSpeciesExtinct[s.id]) {
                    vbox.getChildren().remove(s.fertilitySpecies);
                    vbox.getChildren().remove(s.sightSpecies);
                    vbox.getChildren().remove(s.metabolismSpecies);
                    s.speciesName.setText("Species " + s.name + ": DEAD");
                    s.speciesName.getStyleClass().add("labelWarning");
                    isSpeciesExtinct[s.id] = true;
                }
                // FILL IN populationStats
                b.populationStats.get(s.id).add(s.animalList.size());
            }
            // FILL IN geneStats
            b.geneStats.get(0).add(b.avgFertility);
            b.geneStats.get(1).add(b.avgMetabolism);
            b.geneStats.get(2).add(b.avgSight);
            // CLIP IF NECESSARY
            if(b.geneStats.get(0).size() >= 32) {
                for (int i = 0; i < Species.speciesCreated; i++)
                    clip(b.populationStats.get(i));
                for (int i = 0; i < Animal.GENECOUNT; i++)
                    clip(b.geneStats.get(i));
            }
        }
    }

    public static void clip(LinkedList<?> list) {
        // REDUCES ELEMENTS OF LIST BY HALF
        ListIterator<?> li = list.listIterator();
        while(li.hasNext()) {
            li.next();
            li.remove();
            if(li.hasNext())
                li.next();
        }
    }

    public void drawAnimal(Animal a, int x, int y) {
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        if(a.isEgg())
            gc.fillOval(M*x+10, M*y+10, 15, 20);
        else
        {
            //gc.drawImage(a.species.images[a.direction], x, y);
            ImageView iv = new ImageView(a.species.images);
            iv.setRotate(a.direction*90);
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.YELLOWGREEN);
            if(b.fields[y][x].isWater) params.setFill(Color.LIGHTBLUE);
            gc.drawImage(iv.snapshot(params, null), M*x, M*y);
        }
    }

    public void drawEnd() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("endview.fxml"));
        try {
            Parent root = loader.load();
            Scene endScene = new Scene(root);
            endScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            Main.primaryStage.setScene(endScene);
            Main.primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
