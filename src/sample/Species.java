package sample;

import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.util.LinkedList;

public class Species {
    static int speciesCreated = 0;
    final int id;

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
    //abilities
    boolean canSwim;
    boolean carrionFeeder;
    // displaying
    String name;
    String chartColor;
    Image images;
    // fields required for BoardView
    public Label speciesName;
    public Label fertilitySpecies;
    public Label sightSpecies;
    public Label metabolismSpecies;
    public Species()
    {
        id = speciesCreated++;
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
        canSwim=false;
        carrionFeeder=false;
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
