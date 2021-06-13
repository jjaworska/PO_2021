package sample;

import java.util.Random;

public class Field {
    Random rand;
    int foodFrequency;
    Animal animal;
    boolean food;
    boolean obstacle;
    boolean tree;
    boolean isWater;
    int carrion;
    boolean drying;
    Field(Random rand, int foodFrequency) {
        this.rand=rand;
        this.foodFrequency = foodFrequency;
        this.animal = null;
        food = false;
        obstacle=false;
        tree = false;
        isWater=false;
        drying=false;
        carrion =0;
    }
    boolean isFree() {
        return animal == null && !obstacle && !tree;
    }
    boolean isFree(Species s)
    {
        if(animal != null) return false;
        if(obstacle) return false;
        if(tree) return false;
        if(isWater && ! s.canSwim) return false;
        return carrion <= 0 || s.carrionFeeder;
    }
    void generateFood(){
        if(isFree() && !food && carrion==0){
            int x = rand.nextInt(foodFrequency);
            if(x == 1) food = true;
        }
        if(carrion>0){
            carrion=(carrion+1)%4;
        }
    }
}
