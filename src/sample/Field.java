package sample;

import java.util.Random;

public class Field {
    Random rand;
    int foodFrequency;
    Animal animal;
    boolean hasFood;
    boolean obstacle;
    Field(Random rand, int foodFrequency) {
        this.rand=rand;
        this.foodFrequency = foodFrequency;
        this.animal = null;
        hasFood = false;
        obstacle=false;
    }
    boolean isFree() {
        return animal == null && !obstacle;
    }
    void generateFood(){
        if(isFree() && !hasFood){
            int x = rand.nextInt(foodFrequency);
            if(x == 1) hasFood = true;
        }
    }
}
