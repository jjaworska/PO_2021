package sample;

import java.util.Random;

public class Animal {
    // CONSTANTS
    static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    static final int GENECOUNT = 4;
    static final String[] GENENAME = {"fertility", "metabolism speed", "sight", "minimum lifespan"};
    static final int FertilityId = 0;
    static final int MetabolismId = 1;
    static final int SightId = 2;
    static final int LifespanId = 3;
    // RANDOMIZATION PARAMETERS
    static Random rg = new Random();
    // PROPERTIES
    Species species;
    int age = 0;
    boolean alive = true;
    int direction;
    // NEEDS
    public float hunger;
    // GENES
    float[] genes;

    float mutateValue(float x) {
        float variance = (x < 100 ? species.mutationCoefficient : species.mutationCoefficient / 2);
        float r = rg.nextFloat();
        float min = x * (1.0f - variance);
        if(min < 0) min = 0;
        float max = x * (1.0f + variance);
        return min + (max - min) * r;
    }

    Animal(Species species) {
        // constructs an animal from specified species
        this.species=species;
        this.direction = rg.nextInt(4);
        this.hunger = species.maxHunger;
        this.genes = species.geneSpeciesValue;
    }

    Animal getDescendant() {
        Animal a = new Animal(this.species);
        a.direction = rg.nextInt(4);
        for (int i = 0; i < GENECOUNT; i++)
            a.genes[i] = mutateValue(this.genes[i]);
        return a;
    }

    public boolean step(Field f) {
        Random rg = new Random();
        age++;
        if(!isEgg())
            hunger -= genes[MetabolismId];
        if(age > genes[LifespanId]) {
            // old animals die with increasing probability
            if (rg.nextInt((int) genes[LifespanId]) + genes[LifespanId] <= age)
                alive = false;
        }
        if(!species.canSwim && f.isWater)
            alive=false;
        if (hunger < 0)
            alive = false;
        return alive;
    }

    public boolean isEgg() {
        return age * 10 < genes[LifespanId];
    }

    public boolean isYoung() {
        return !isEgg() && age * 3 < genes[LifespanId];
    }
}
