package sample;

import java.util.*;

import static java.lang.Math.abs;

public class Board {
    float avgSight;
    float avgFertility;
    float avgMetabolism;
    float starterSight;
    float starterFertility;
    float starterMetabolism;
    int starterAnimalCount;
    int animalCount;

    // fields of the Board class
    int height;
    int width;
    public int stepCount = 0;
    Field[][] fields;
    LinkedList<Species> speciesList;
    LinkedList<LinkedList<Integer>> populationStats;
    LinkedList<LinkedList<Float>> geneStats;

    Random rand;

    Board(int height, int width, int foodFrequency, int obstaclesCount, int speciesCount) {
        this.height = height;
        this.width = width;
        this.rand = new Random();

        geneStats = new LinkedList<>();
        for (int i = 0; i < Animal.GENECOUNT; i++)
            geneStats.add(new LinkedList<>());
        populationStats = new LinkedList<>();
        for (int i = 0; i < speciesCount; i++) {
            populationStats.add(new LinkedList<>());
        }

        fields = new Field[height][width];
        for (int x = 0; x < height; x++)
            for (int y = 0; y < width; y++)
                fields[x][y] = new Field(rand, 300 - 25*foodFrequency);

        speciesList = new LinkedList<>();

        while (obstaclesCount-- > 0)
            generateObstacle();
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
    void generateAnimals(int number, Species species) {
        starterAnimalCount += number;
        while(number-- >0) {
            boolean success = false;
            while (!success) {
                int x = rand.nextInt(height);
                int y = rand.nextInt(width);
                if (fields[x][y].isFree()) {
                    fields[x][y].animal = new Animal(species);
                    species.animalList.add(new Pair(x, y));
                    success = true;
                }
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
        Pair copyP = new Pair(p);
        Animal a = P.animal;
        if (Q.hasFood) {
            a.hunger = 100;
            Q.hasFood = false;
        }
        Q.animal = a;
        P.animal = null;
        p.x = q.x;
        p.y = q.y;
        if (a.hunger > 80 && !a.isYoung()) {
            // lays an egg with probability fertility%
            if (rand.nextInt(100) < a.fertility) {
                P.animal = a.getDescendant();
                a.species.descendantsList.add(copyP);
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
        for (int x = 0; x < height; x++)
            for (int y = 0; y < width; y++)
                fields[x][y].generateFood();
        animalCount=0;
        for (Species species : speciesList) {
            species.sumSight = 0;
            species.sumFertility = 0;
            species.sumMetabolism = 0;
            for (Iterator<Pair> it = species.animalList.iterator(); it.hasNext(); ) {
                Pair p = it.next();
                Animal a = fieldAt(p).animal;
                if (!a.step()) {
                    fieldAt(p).animal = null;
                    it.remove();
                    continue;
                }
                species.sumSight += a.sight;
                species.sumFertility += a.fertility;
                species.sumMetabolism += a.metabolismSpeed;
                animalCount++;
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

                        List<Integer> dirs = new ArrayList<>();
                        dirs.add(a.direction);
                        for (int i = 0; i < 4; i++)
                            if (i != a.direction && i != 3 - a.direction)
                                dirs.add(i);
                        dirs.add(3 - a.direction);

                        Iterator<Integer> itDirs = dirs.iterator();
                        while (!moved && itDirs.hasNext()) {
                            switch (itDirs.next()) {
                                case Animal.UP:
                                    if (goUp(p)) {
                                        moved = true;
                                        a.direction = Animal.UP;
                                    }
                                    break;
                                case Animal.DOWN:
                                    if (goDown(p)) {
                                        moved = true;
                                        a.direction = Animal.DOWN;
                                    }
                                    break;
                                case Animal.LEFT:
                                    if (goLeft(p)) {
                                        moved = true;
                                        a.direction = Animal.LEFT;
                                    }
                                    break;
                                case Animal.RIGHT:
                                    if (goRight(p)) {
                                        moved = true;
                                        a.direction = Animal.RIGHT;
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
            species.addDescendants();
        }

        float sumSight = 0.0f;
        float sumFertility = 0.0f;
        float sumMetabolism = 0.0f;
        for(Species s : speciesList) {
            sumSight += s.sumSight;
            sumFertility += s.sumFertility;
            sumMetabolism += s.sumMetabolism;
        }
        if(animalCount!=0) {
            avgMetabolism = sumMetabolism/animalCount;
            avgFertility = sumFertility/animalCount;
            avgSight = sumSight/animalCount;
        }

        if(stepCount == 0)
        {
            starterFertility=avgFertility;
            starterMetabolism=avgMetabolism;
            starterSight=avgSight;
        }
        stepCount++;

        return animalCount>0;
    }
}
