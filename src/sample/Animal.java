package sample;

import java.util.Random;

public class Animal {
    // CONSTANTS
    static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    static final int GENECOUNT = 3;
    static final String[] GENENAME = {"fertility", "metabolism speed", "sight"};
    static final int FertilityId = 0;
    static final int MetabolismId = 1;
    static final int SightId = 2;
    // RANDOMIZATION PARAMETERS
    static Random rg = new Random();
    static float mutationCoefficient = 0.1f;
    // PROPERTIES
    Species species;
    int age = 0;
    boolean alive = true;
    int direction;
    // NEEDS
    public float hunger;
    // GENES
    float[] genes;

    static float mutateValue(float x) {
        float r = rg.nextFloat();
        float min = x * (1.0f - mutationCoefficient);
        float max = x * (1.0f + mutationCoefficient);
        return min + (max - min) * r;
    }

    Animal(Species species) {
        // constructs an animal from specified species
        this.species=species;
        this.direction = rg.nextInt(4);
        this.hunger = species.hunger;
        this.genes = species.geneSpeciesValue;
    }

    Animal getDescendant() {
        Animal a = new Animal(this.species);
        a.direction = rg.nextInt(4);
        for (int i = 0; i < GENECOUNT; i++)
            a.genes[i] = mutateValue(this.genes[i]);
        return a;
    }

    public boolean step() {
        Random rg = new Random();
        age++;
        hunger -= genes[MetabolismId];
        if(age > species.minimumLifespan) {
            // old animals die with increasing probability
            if (rg.nextInt(species.minimumLifespan) + species.minimumLifespan <= age)
                alive = false;
        }
        if (hunger < 0)
            alive = false;
        return alive;
    }
    public void addStats() {
        for(int i = 0; i < GENECOUNT; i++) {
            species.geneSpeciesSum[i] += this.genes[i];
        }
    }

    public boolean isEgg() {
        return age * 10 < species.minimumLifespan;
    }

    public boolean isYoung() {
        return !isEgg() && age * 3 < species.minimumLifespan;
    }
}
