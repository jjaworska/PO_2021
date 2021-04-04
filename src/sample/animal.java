package sample;

public class animal {
    public boolean alive;
    public char name;
    static char number = 'A';
    // needs
    public int hunger;
    // genes
    public int sight;
    public int metabolism_speed; // indicates how fast an animal burns calories

    animal() {
        // constructs an animal with default traits
        //position = new pair(x, y);
        hunger = 100;
        sight = 7;
        metabolism_speed = 10;
        alive = true;
        name = number++;
    }

    public boolean step() {
        hunger -= metabolism_speed;
        if(hunger < 0)
            alive = false;
        return alive;
    }
}
