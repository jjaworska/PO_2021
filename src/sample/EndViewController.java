package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class EndViewController implements Initializable {
    @FXML
    AnchorPane pane;
    @FXML
    TextFlow information;
    @FXML
    LineChart<Number, Number> populationChart;
    @FXML
    NumberAxis xAxisPopulation;
    @FXML
    NumberAxis yAxisPopulation;
    @FXML
    LineChart<Number, Number> genesChart;
    @FXML
    NumberAxis xAxisGenes;
    @FXML
    NumberAxis yAxisGenes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);

        Text endgame = new Text("Simulation ended after " + BoardView.b.stepCount + " steps \n");
        endgame.setFill(Color.web("#494949"));
        Text counter = new Text("Number of animals:\n"+ BoardView.b.starterAnimalCount+" --> "+BoardView.b.animalCount + "\n");
        counter.setFill(Color.web("#494949"));
        information.getChildren().addAll(endgame, counter);
        for(int i = 0; i < Animal.GENECOUNT; i++) {
            Text toAdd = new Text("Evolution of " + Animal.GENENAME[i] + ": " +
                    BoardView.df.format(BoardView.b.starterGeneStats[i]) + " --> " +
                    BoardView.df.format(BoardView.b.avgGeneValue[i] * (i == Animal.LifespanId ? 9 : 1)) + "\n"
            );
            toAdd.setFill(Color.web("#494949"));
            information.getChildren().add(toAdd);
        }

        populationChart.setTitle("Population of species over time");
        int i=0;
        for(Species s : BoardView.b.speciesList) {
            XYChart.Series<Number, Number> toAdd = new XYChart.Series<>();
            toAdd.setName(s.name);
            int cnt = 0;
            for(Integer y : BoardView.b.populationStats2.get(s.id))
                toAdd.getData().add(new XYChart.Data<>(cnt++, y));

            populationChart.getData().add(toAdd);
            changeColor(i, s.chartColor);
            i++;
        }
        populationChart.setCreateSymbols(false);
        populationChart.setLegendVisible(true);

        genesChart.setTitle("Evolution of genes");

        for (i = 0; i < Animal.GENECOUNT; i++) {
            XYChart.Series<Number, Number> toAdd = new XYChart.Series<>();
            toAdd.setName(Animal.GENENAME[i]);
            int cnt = 0;
            for(Float y : BoardView.b.geneStats.get(i))
                toAdd.getData().add(new XYChart.Data<>(cnt++, y));
            genesChart.getData().add(toAdd);
        }
        genesChart.setCreateSymbols(false);
        genesChart.setLegendVisible(true);
    }

    private void changeColor(int position, String color) {
        Platform.runLater(() -> {
            Node nl = populationChart.lookup(".default-color" + position + ".chart-series-line");
            Node ns = populationChart.lookup(".default-color" + position + ".chart-line-symbol");
            Node nsl = populationChart.lookup(".default-color" + position + ".chart-legend-item-symbol");

            nl.setStyle("-fx-stroke: " + color + ";");
            ns.setStyle("-fx-background-color: " + color + ", white;");
            nsl.setStyle("-fx-background-color: " + color + ", white;");
        });
    }

    public void newSimulation() {
        Species.SPECIESCREATED = 0;
        try {
            WelcomeView welcome = new WelcomeView();
            Scene welcomeScene = new Scene(welcome);
            welcomeScene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm()
            );
            Main.primaryStage.setScene(welcomeScene);
            Main.primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
