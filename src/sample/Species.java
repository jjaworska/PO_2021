package sample;

import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.util.LinkedList;

public class Species {
    String name;
    float fertility;
    float sight;
    float metabolismSpeed;
    int hunger;
    static int minimumLifespan = 40;
    float sumSight = 0;
    float sumFertility = 0;
    float sumMetabolism = 0;
    LinkedList<Pair> animalList;
    LinkedList<Pair> descendantsList;
    Image images;
    // fields required for BoardView
    public Label speciesName;
    public Label fertilitySpecies;
    public Label sightSpecies;
    public Label metabolismSpecies;
    public Species()
    {
        animalList=new LinkedList<>();
        descendantsList=new LinkedList<>();
        sight=7;
        hunger=100;
        fertility=14;
        metabolismSpeed=10;
        speciesName = new Label("Species");
        fertilitySpecies = new Label("  fertility");
        sightSpecies = new Label("  sight");
        metabolismSpecies = new Label("  metabolism speed");
    }
    public boolean isExtinct() {
        return animalList.isEmpty() && descendantsList.isEmpty();
    }
    public void addDescendants()
    {
        while (!descendantsList.isEmpty())
            animalList.add(descendantsList.remove());
    }
}
