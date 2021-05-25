package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class TestController implements Initializable {
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
        Text endgame = new Text("Simulation ended after " + BoardView.b.stepCount + " steps \n");
        Text counter = new Text("Number of animals:\n"+ BoardView.b.starterAnimalCount+" --> "+BoardView.b.animalCount + "\n");
        Text fertilityFinal = new Text("Evolution of fertility:\n"+BoardView.df.format( BoardView.b.starterFertility)+" --> "+BoardView.df.format(BoardView.b.avgFertility) + "\n");
        Text metabolismSpeedFinal = new Text("Evolution of metabolism speed:\n"+BoardView.df.format(BoardView.b.starterMetabolism)+" --> "+BoardView.df.format(BoardView.b.avgMetabolism) + "\n");
        Text sightFinal = new Text("Evolution of sight:\n"+BoardView.df.format(BoardView.b.starterSight)+" --> "+BoardView.df.format(BoardView.b.avgSight) + "\n");
        information.getChildren().addAll(endgame, counter, fertilityFinal, metabolismSpeedFinal, sightFinal);
        for(Node x : information.getChildren())
            if(x instanceof Text)
                ((Text) x).setFill(Color.web("#F6E8EA"));

        populationChart.setTitle("Population of species over time");

        for(Species s : BoardView.b.speciesList) {
            XYChart.Series<Number, Number> toAdd = new XYChart.Series<>();
            toAdd.setName(s.name);
            int cnt = 0;
            for(Integer y : BoardView.b.populationStats.get(s.id)) {
                XYChart.Data<Number, Number> point = new XYChart.Data<>(cnt++, y);
                /*Node lineSymbol = point.getNode().lookup(".chart-line-symbol");
                lineSymbol.setStyle("-fx-background-color: " + s.chartColor + ";");*/
                toAdd.getData().add(new XYChart.Data<>(cnt++, y));
            }
            populationChart.getData().add(toAdd);
            Node line = toAdd.getNode().lookup(".chart-series-line");
            line.setStyle("-fx-stroke: " + s.chartColor + ";");
            //toAdd.getNode().setStyle("-fx-stroke: " + s.chartColor + ";\n -fx-background-color: " + s.chartColor + ";");
        }
        populationChart.setCreateSymbols(false);
        populationChart.setLegendVisible(true);

        genesChart.setTitle("Evolution of genes");

        for (int i = 0; i < Animal.GENECOUNT; i++) {
            XYChart.Series<Number, Number> toAdd = new XYChart.Series<>();
            toAdd.setName(Animal.GENENAME[i]);
            int cnt = 0;
            for(Float y : BoardView.b.geneStats.get(i)) {
                XYChart.Data<Number, Number> point = new XYChart.Data<>(cnt++, y);
                toAdd.getData().add(new XYChart.Data<>(cnt++, y));
            }
            genesChart.getData().add(toAdd);
        }
        genesChart.setCreateSymbols(false);
        genesChart.setLegendVisible(true);

        for (LinkedList<Integer> stat : BoardView.b.populationStats) {
            System.out.println(stat);
        }
        for (int i = 0; i < 3; i++)
            System.out.println(BoardView.b.geneStats.get(i));
    }
}
