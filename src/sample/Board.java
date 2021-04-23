package sample;

import java.util.*;

import static java.lang.Math.abs;

public class Board {
    public static class Pair {
        int x; int y;
        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public static class Field {
        Animal animal;
        boolean hasFood;
        boolean obstacle;
        Field() {
            this.animal = null;
            hasFood = false;
            obstacle=false;
        }
        boolean isFree() {
            return animal == null && !obstacle;
        }
    }

    // fields of the Board class
    int height;
    int width;
    public int stepCount = 0;
    Field[][] fields;
    LinkedList<Pair> animalList;
    LinkedList<Pair> animalsToAdd;
    Random rand;

    Board(int height, int width, int animalCount, int foodCount, int obstaclesCount) {
        this.height = height;
        this.width = width;
        this.rand = new Random();

        fields = new Field[height][width];
        for (int x = 0; x < height; x++)
            for (int y = 0; y < width; y++)
                fields[x][y] = new Field();

        animalsToAdd = new LinkedList<>();
        animalList = new LinkedList<>();

        while (obstaclesCount-- > 0)
            generateObstacle();

        while (animalCount-- > 0)
            generateAnimal();

        while (foodCount-- > 0)
            generateFood();
    }

    // Three similar functions used in constructor
    void generateObstacle(){
        boolean success = false;
        while (!success) {
            int x = rand.nextInt(height);
            int y = rand.nextInt(width);
            if (fields[x][y].isFree()) {
                fields[x][y].obstacle=true;
                success = true;
            }
        }
    }
    void generateAnimal() {
        boolean success = false;
        while (!success) {
            int x = rand.nextInt(height);
            int y = rand.nextInt(width);
            if (fields[x][y].isFree()) {
                fields[x][y].animal = new Animal();
                animalList.add(new Pair(x, y));
                success = true;
            }
        }
    }
    void generateFood() {
        boolean success = false;
        while (!success) {
            int x = rand.nextInt(height);
            int y = rand.nextInt(width);
            if (fields[x][y].isFree() && !fields[x][y].hasFood) {
                fields[x][y].hasFood = true;
                success = true;
            }
        }
    }

    Field fieldAt(Pair p) {
        return fields[p.x][p.y];
    }

    Collection<Pair> findFood(Pair p, int d) {
        // returns list of all points q such that
        // - distance between q and p equals d
        // - there is food on this field
        List<Pair> list = new LinkedList<>();
        int x = p.x; int y = p.y;
        for (int i = Math.max(0, x - d); i <= Math.min(height - 1, x + d); i++) {
            int j = y - d + abs(i - x);
            if (j >= 0 && fields[i][j].hasFood)
                list.add(new Pair(i, j));
            j = y + d - abs(i - x);
            if (j < width && fields[i][j].hasFood)
                list.add(new Pair(i, j));
        }
        return list;
    }

    public void move(Pair p, Pair q) {
        // describes movement of an animal from location p to q
        Field P = fieldAt(p);
        Field Q = fieldAt(q);
        Pair copyP = new Pair(p.x, p.y);
        Animal a = P.animal;
        if (Q.hasFood) {
            a.hunger = 100;
            Q.hasFood = false;
            Q.animal = a;
            P.animal = null;
            p.x = q.x; p.y = q.y;
            generateFood();
        }
        else {
            Q.animal = a;
            P.animal = null;
            p.x = q.x; p.y = q.y;
        }
        if (!a.isYoung() && a.hunger > 80) {
            // lays an egg with probability fertility%
            if (rand.nextInt(100) < a.fertility) {
                P.animal = a.getDescendant();
                animalsToAdd.add(copyP);
            }
        }
    }

    public boolean goUp(Pair p) {
        Pair q = new Pair(p.x - 1, p.y);
        if (q.x >= 0 && fieldAt(q).isFree()) {
            move(p, q);
            return true;
        }
        return false;
    }
    public boolean goDown(Pair p) {
        Pair q = new Pair(p.x + 1, p.y);
        if (q.x < height && fieldAt(q).isFree()) {
            move(p, q);
            return true;
        }
        return false;
    }
    public boolean goLeft(Pair p) {
        Pair q = new Pair(p.x, p.y - 1);
        if (q.y >= 0 && fieldAt(q).isFree()) {
            move(p, q);
            return true;
        }
        return false;
    }
    public boolean goRight(Pair p) {
        Pair q = new Pair(p.x, p.y + 1);
        if (q.y < width && fieldAt(q).isFree()) {
            move(p, q);
            return true;
        }
        return false;
    }

    public boolean makeStep() {
        for (Iterator<Pair> it = animalList.iterator(); it.hasNext();) {
            Pair p = it.next();
            Animal a = fieldAt(p).animal;
            if (!a.step()) {
                fieldAt(p).animal = null;
                it.remove();
                continue;
            }
            if (a.isEgg()) {
                continue;
            }
            if (!a.isEgg()) {
                boolean moved = false;
                for (int d = 1; d < a.sight && !moved; d++)
                    for (Pair q : findFood(p, d)) {
                        if (q.x < p.x && goUp(p)) {
                            a.direction = Animal.UP;
                            moved = true;
                            break;
                        }
                        if (q.x > p.x && goDown(p)) {
                            a.direction = Animal.DOWN;
                            moved = true;
                            break;
                        }
                        if (q.y > p.y && goRight(p)) {
                            a.direction = Animal.RIGHT;
                            moved = true;
                            break;
                        }
                        if (q.y < p.y && goLeft(p)) {
                            a.direction = Animal.LEFT;
                            moved = true;
                            break;
                        }
                    }
                if (!moved) {
                    // try to move in a random direction
                    // preferably go ahead and not backwards

                    List<Integer> dirs = new ArrayList<Integer>();
                    dirs.add(a.direction);
                    for (int i = 0; i < 4; i++)
                        if (i != a.direction && i != 3 - a.direction)
                            dirs.add(i);
                    dirs.add(3 - a.direction);

                    Iterator<Integer> itDirs = dirs.iterator();
                    while (!moved && itDirs.hasNext()) {
                        switch (itDirs.next()) {
                            case Animal.UP:
                                if (goUp(p))
                                    moved = true;
                                break;
                            case Animal.DOWN:
                                if (goDown(p))
                                    moved = true;
                                break;
                            case Animal.LEFT:
                                if (goLeft(p))
                                    moved = true;
                                break;
                            case Animal.RIGHT:
                                if (goRight(p))
                                    moved = true;
                                break;
                        }
                    }
                }
            }
        }

        stepCount++;
        while (!animalsToAdd.isEmpty())
            animalList.add(animalsToAdd.remove());

        return !animalList.isEmpty();
    }
}
