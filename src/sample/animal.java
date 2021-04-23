package sample;

import java.util.Random;

public class animal {
    public boolean alive;
    public int direction;
    static int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3;
    //public char name;
    static char number = 'A';
    // needs
    public int hunger;
    // genes
    public int sight;
    public int metabolism_speed; // indicates how fast an animal burns calories

    animal() {
        // constructs an animal with default traits
        Random rg = new Random();
        direction = rg.nextInt(4);
        hunger = 100;
        sight = 7;
        metabolism_speed = 10;
        alive = true;
    }

    public boolean step() {
        Random rg = new Random();
        direction = rg.nextInt(4);
        hunger -= metabolism_speed;
        if(hunger < 0)
            alive = false;
        return alive;
    }
}
