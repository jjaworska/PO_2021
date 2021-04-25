package sample;

import java.util.Random;

public class Animal {
    static final int LEFT = 0, UP = 1, DOWN = 2, RIGHT = 3;
    static Random rg = new Random();
    static int minimumLifespan = 40;
    static float mutationCoefficient = 0.2f;

    int age = 0;
    boolean alive = true;
    int direction;
    // needs
    public int hunger;
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

    Animal() {
        // constructs an animal with default traits
        this.direction = rg.nextInt(4);
        this.hunger = 100;
        this.sight = mutateValue(7);
        this.metabolismSpeed = mutateValue(10);
        this.fertility = mutateValue(5);
    }

    Animal getDescendant() {
        System.out.println("Descendant came");
        Animal a = new Animal();
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
        if(age > minimumLifespan) {
            // old animals die with increasing probability
            if (rg.nextInt(minimumLifespan) + minimumLifespan <= age)
                alive = false;
        }
        if (hunger < 0)
            alive = false;
        return alive;
    }

    public boolean isEgg() {
        return age * 10 < minimumLifespan;
    }

    public boolean isYoung() {
        return !isEgg() && age * 3 < minimumLifespan;
    }
}
