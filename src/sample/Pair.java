package sample;

public class Pair {
    int x; int y;
    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Pair(Pair p)
    {
        this.x=p.x;
        this.y=p.y;
    }
}
