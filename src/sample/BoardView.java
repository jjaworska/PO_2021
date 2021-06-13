package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


public class BoardView {
    static DecimalFormat df = new DecimalFormat("#.##");
    static Image food_img = new Image(String.valueOf(BoardView.class.getResource("img/plant.png")));
    static Image obstacle_img = new Image(String.valueOf(BoardView.class.getResource("img/obstacle2.png")));
    static Image dead_img = new Image(String.valueOf(BoardView.class.getResource("img/dead.png")));
    static Image tree_img = new Image(String.valueOf(BoardView.class.getResource("img/flower.png")));
    static int M = 40;
    static Board b;
    private boolean[] isSpeciesExtinct;
    LinkedList<LinkedList<Integer>> populationStats1;
    @FXML
    public Canvas canvas;
    public Canvas background;
    @FXML
    public Button StartButton;
    @FXML
    public Button PauseButton;
    @FXML
    public Button StopButton;
    @FXML
    public VBox vbox;
    @FXML
    public VBox insideVbox;
    @FXML
    public VBox animalVbox;
    @FXML
    public Label fertilityLabel;
    @FXML
    public Label metabolismSpeedLabel;
    @FXML
    public Label sightLabel;
    @FXML
    public Label speciesLabel;
    @FXML
    public Label ageLabel;
    @FXML
    public ProgressBar hungerBar;
    @FXML
    public MenuItem lakeEvent;
    @FXML
    public MenuItem lakesDrying;
    @FXML
    public MenuItem cropFailure;
    @FXML
    public LineChart<String, Integer> runtimePopulationChart;
    List<XYChart.Series<String, Integer>> runtimePopulationChartSeries;
    final static int MAX_CHART_SIZE = 10;

    Timeline timeline;

    public static final Color grassColor = Color.web("86bb8c");
    public static final Color waterColor = Color.LIGHTBLUE;

    public void init(Board b) {
        this.b = b;
        //backgroundGc = this.canvas.getGraphicsContext2D();
        this.timeline = new Timeline(new KeyFrame(Duration.millis(200), a -> {
            if (b.makeStep())
                step();
            else {
                drawEnd();
                timeline.stop();
            }
        }));
        this.background.setWidth(b.width * M);
        this.background.setHeight(b.height * M);
        this.canvas.setWidth(b.width * M);
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
        populationStats1 = new LinkedList<>();
        for (int i = 0; i < speciesNum; i++)
            populationStats1.add(new LinkedList<>());
        int cnt = 0;
        for (Species s : b.speciesList) {
            insideVbox.getChildren().add(s.speciesName);
            s.speciesName.setText("Species " + s.name);
            for (int i = 0; i < 3; i++) {
                insideVbox.getChildren().add(s.geneSpeciesLabel[i]);
            }
        }
        drawBackground();
        animalVbox.setVisible(false);
        canvas.setOnMouseClicked(e -> {
            Field f = b.fields[(int) e.getY() / M][(int) e.getX() / M];
            if (f.animal != null) {
                b.currentAnimal = f.animal;
                animalVbox.setVisible(true);
                speciesLabel.setText(b.currentAnimal.species.name);
                ageLabel.setText("age: " + b.currentAnimal.age);
                hungerBar.setProgress(b.currentAnimal.hunger / b.currentAnimal.species.maxHunger);
            }
        });
        lakeEvent.setOnAction(e -> {
            b.generateLake();
            drawBackground();
        });
        lakesDrying.setOnAction(e -> {
            Events.drying(b);
            drawBackground();
        });
        cropFailure.setOnAction(e -> {
            Events.cropFailure(b);
            drawBackground();
        });

        runtimePopulationChartSeries = new LinkedList<>();
        runtimePopulationChart.setCreateSymbols(false);
        cnt = 0;
        for (Species s : b.speciesList) {
            runtimePopulationChartSeries.add(new XYChart.Series<String, Integer>());
            XYChart.Series<String, Integer> newSeries = runtimePopulationChartSeries.get(cnt);
            runtimePopulationChart.getData().add(newSeries);
            changeColor(cnt, s.chartColor);
            cnt++;
        }
    }

    private void changeColor(int position, String color) {
        Platform.runLater(() -> {
            Node nl = runtimePopulationChart.lookup(".default-color" + position + ".chart-series-line");
            Node ns = runtimePopulationChart.lookup(".default-color" + position + ".chart-line-symbol");
            //Node nsl = populationChart.lookup(".default-color" + position + ".chart-legend-item-symbol");

            nl.setStyle("-fx-stroke: " + color + ";");
            ns.setStyle("-fx-background-color: " + color + ", white;");
            //nsl.setStyle("-fx-background-color: " + color + ", white;");
        });
    }

    // DRAWS GRASS, WATER, OBSTACLES AND TREES (AKA FLOWERS)
    public void drawBackground() {
        GraphicsContext gc = background.getGraphicsContext2D();
        for (int i = 0; i < b.height; i++)
            for(int j = 0; j < b.width; j++) {
                if (b.fields[i][j].isWater) {
                    gc.setFill(waterColor);
                    gc.fillRect(M * j, M * i, M, M);
                }
                else {
                    gc.setFill(grassColor);
                    gc.fillRect(M*j, M*i, M, M);
                }
                for(int t = 0; t < 4; t++)
                    checkField(i, j, t, gc); // DEALING WITH LAKE EDGES
                if(b.fields[i][j].obstacle) {
                    gc.drawImage(obstacle_img, M*j, M*i);
                }
                if(b.fields[i][j].tree){
                    gc.drawImage(tree_img, M*j, M*i);
                }
            }
    }

    // CHECKES IF A FIELD SHOULD BE ROUNDED
    void checkField(int i, int j, int which, GraphicsContext gc) {
        int different = 0;
        int di = (which < 2 ? -1 : 1);
        int dj = ((which == 0 || which == 3) ? 1 : -1);
        if(i + di >= 0 && i + di < b.height)
            different += (b.fields[i][j].isWater != b.fields[i + di][j].isWater ? 1 : 0);
        if(j + dj >= 0 && j + dj < b.width)
            different += (b.fields[i][j].isWater != b.fields[i][j + dj].isWater ? 1 : 0);
        if(different == 2) {
            if(b.fields[i + di][j + dj].isWater != b.fields[i][j].isWater)
                drawCorner(gc, i, j, which, b.fields[i][j].isWater);
            else if(!b.fields[i][j].isWater)
                drawCorner(gc, i, j, which, b.fields[i][j].isWater);
        }
    }

    public void drawCorner(GraphicsContext gc, int i, int j, int which, boolean isWater) {
        int m = M / 2;
        if (isWater) {
            gc.setFill(grassColor);
        } else {
            gc.setFill(waterColor);
        }
        switch(which) {
            case 0:
                gc.fillRect(M * j + m, M * i, m, m);
                break;
            case 1:
                gc.fillRect(M * j, M * i, m, m);
                break;
            case 2:
                gc.fillRect(M * j, M * i + m, m, m);
                break;
            case 3:
                gc.fillRect(M * j + m, M * i + m, m, m);
        }
        if (isWater) {
            gc.setFill(waterColor);
        } else {
            gc.setFill(grassColor);
        }
        gc.fillArc(M * j, M * i, M, M, which * 90, 90, ArcType.ROUND);
    }

    // DRAWS ANIMALS AND FOOD
    public void draw()
    {
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, M*b.width, M*b.height);
        for(int i=0;i<b.height; i++)
        {
            for(int j=0; j<b.width; j++)
            {
                if(b.fields[i][j].animal != null) {
                    drawAnimal(b.fields[i][j].animal, j, i);
                }
                else if(b.fields[i][j].food) {
                    gc.drawImage(food_img, M*j, M*i);
                }
                if(b.fields[i][j].carrion>0){
                    gc.drawImage(dead_img, M*j, M*i);
                }
            }
        }
    }

    public void step() {
        draw();
        if (b.stepCount % 5 == 0) {
            fertilityLabel.setText("  Fertility: " + df.format(b.avgGeneValue[Animal.FertilityId]));
            metabolismSpeedLabel.setText("  Metabolism speed: " + df.format(b.avgGeneValue[Animal.MetabolismId]));
            sightLabel.setText("  Sight: " + df.format(b.avgGeneValue[Animal.SightId]));
            int speciesCnt = 0;
            for (Species s : b.speciesList) {
                populationStats1.get(speciesCnt).add(s.animalList.size());
                if (b.stepCount > 50)
                    populationStats1.get(speciesCnt).remove(0);
                if (!s.isExtinct()) {
                    for (int i = 0; i < Animal.GENECOUNT; i++) {
                        s.geneSpeciesLabel[i].setText("  " + Animal.GENENAME[i] + ": " +
                                df.format(s.geneSpeciesSum[i] / s.animalList.size()));
                    }
                } else if (!isSpeciesExtinct[s.id]) {
                    for (int i = 0; i < Animal.GENECOUNT; i++) {
                        insideVbox.getChildren().remove(s.geneSpeciesLabel[i]);
                    }
                    s.speciesName.setText("Species " + s.name + ": EXTINCT");
                    s.speciesName.getStyleClass().add("labelWarning");
                    isSpeciesExtinct[s.id] = true;
                }
                // FILL IN populationStats
                b.populationStats2.get(s.id).add(s.animalList.size());
            }
            // FILL IN geneStats
            for (int i = 0; i < Animal.GENECOUNT; i++) {
                b.geneStats.get(i).add(b.avgGeneValue[i]);
            }
            // CLIP IF NECESSARY
            if (b.geneStats.get(0).size() >= 32) {
                for (int i = 0; i < Species.SPECIESCREATED; i++)
                    clip(b.populationStats2.get(i));
                for (int i = 0; i < Animal.GENECOUNT; i++)
                    clip(b.geneStats.get(i));
            }
            // POPULATION_RUNTIME_CHART
            int cnt = 0;
            for(Species s : b.speciesList) {
                if(runtimePopulationChartSeries.get(cnt).getData().size() > MAX_CHART_SIZE)
                    runtimePopulationChartSeries.get(cnt).getData().remove(0);
                runtimePopulationChartSeries.get(cnt).getData().add(new XYChart.Data<>(new Integer(b.stepCount).toString(), s.animalList.size()));
                cnt++;
            }
        }
        if (b.currentAnimal != null) {
            ageLabel.setText("age: " + b.currentAnimal.age);
            hungerBar.setProgress(b.currentAnimal.hunger / b.currentAnimal.species.maxHunger);
        } else {
            animalVbox.setVisible(false);
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
            ImageView iv = new ImageView(a.species.speciesImage);
            iv.setRotate(a.direction*90);
            SnapshotParameters params = new SnapshotParameters();
            // it is transparent now
            params.setFill(Color.TRANSPARENT);
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
