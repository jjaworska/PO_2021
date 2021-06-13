package sample;

import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.util.LinkedList;

public class Species {
    static int SPECIESCREATED = 0;
    final int id;
    // DEFAULT VALUES
    float[] geneSpeciesValue = {7, 14, 10, 80};
    float maxHunger;
    float mutationCoefficient;
    // GENES
    float[] geneSpeciesSum;
    // MEMBERS OF SPECIES
    LinkedList<Pair> animalList;
    LinkedList<Pair> descendantsList;
    // ABILITIES
    boolean canSwim;
    boolean carrionFeeder;
    // DISPLAYING
    String name;
    String chartColor;
    Image speciesImage;
    // FIELDS REQUIRED FOR BOARDVIEW
    public Label speciesName;
    public Label[] geneSpeciesLabel;
    
    public Species()
    {
        id = SPECIESCREATED++;
        animalList=new LinkedList<>();
        descendantsList=new LinkedList<>();
        maxHunger=100;
        speciesName = new Label("Species");
        geneSpeciesSum = new float[Animal.GENECOUNT];
        geneSpeciesLabel = new Label[Animal.GENECOUNT];
        for (int i = 0; i < Animal.GENECOUNT; i++) {
            geneSpeciesLabel[i] = new Label("  " + Animal.GENENAME[i]);
        }
        canSwim=false;
        carrionFeeder=false;
    }
    public boolean isExtinct()
    {
        return animalList.isEmpty() && descendantsList.isEmpty();
    }
    public void addDescendants()
    {
        while (!descendantsList.isEmpty())
            animalList.add(descendantsList.remove());
    }
}
