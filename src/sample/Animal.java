package sample;

import java.util.Random;

public class Animal {
    static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    static final int GENECOUNT = 3;
    static String[] GENENAME = {"Fertility", "Metabolism speed", "Sight"};
    static Random rg = new Random();
    static float mutationCoefficient = 0.1f;
    Species species;
    int age = 0;
    boolean alive = true;
    Pair position;
    int direction;
    // needs
    public float hunger;
    // genes
    float fertility;
    float sight;
    float metabolismSpeed; // indicates how fast an animal burns calories

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
        this.sight = mutateValue(species.sight);
        this.metabolismSpeed = mutateValue(species.metabolismSpeed);
        this.fertility = mutateValue(species.fertility);
    }

    Animal getDescendant() {
        Animal a = new Animal(this.species);
        a.direction = rg.nextInt(4);
        a.fertility = mutateValue(this.fertility);
        a.sight = mutateValue(this.sight);
        a.metabolismSpeed = mutateValue(this.metabolismSpeed);
        return a;
    }

    public boolean step() {
        Random rg = new Random();
        age++;
        //direction = rg.nextInt(4);
        hunger -= metabolismSpeed;
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
        species.sumSight += this.sight;
        species.sumFertility += this.fertility;
        species.sumMetabolism += this.metabolismSpeed;
    }

    public boolean isEgg() {
        return age * 10 < species.minimumLifespan;
    }

    public boolean isYoung() {
        return !isEgg() && age * 3 < species.minimumLifespan;
    }
}
