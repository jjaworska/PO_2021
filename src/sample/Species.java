package sample;

import javafx.scene.image.Image;

import java.util.LinkedList;

public class Species {
    String name;
    float fertility;
    float sight;
    float metabolismSpeed;
    int hunger;
    static int minimumLifespan = 40;
    float sumSight;
    float sumFertility;
    float sumMetabolism;
    LinkedList<Pair> animalList;
    LinkedList<Pair> descendantsList;
    Image image;
    public Species()
    {
        animalList=new LinkedList<>();
        descendantsList=new LinkedList<>();
        sight=7;
        hunger=100;
        fertility=14;
        metabolismSpeed=10;
    }
    public void addDescendants()
    {
        while (!descendantsList.isEmpty())
            animalList.add(descendantsList.remove());
    }
}
