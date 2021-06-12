package sample;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.lang.Math.abs;

public class Board {
    float[] avgGeneValue;

    int starterAnimalCount;
    int animalCount;
    int freeArea;

    // fields of the Board class
    int height;
    int width;
    public int stepCount = 0;
    Field[][] fields;
    LinkedList<Species> speciesList;
    LinkedList<LinkedList<Integer>> populationStats;
    LinkedList<LinkedList<Float>> geneStats;
    float[] starterGeneStats;

    Random rand;

    Board(int height, int width, int foodFrequency, int obstaclesCount, int speciesCount) {
        this.height = height;
        this.width = width;
        this.rand = new Random();

        this.starterGeneStats = new float[Animal.GENECOUNT];
        this.avgGeneValue = new float[Animal.GENECOUNT];
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
                fields[x][y] = new Field(rand, 250 - 20*foodFrequency);

        speciesList = new LinkedList<>();
        freeArea=width*height-obstaclesCount;
        generateLake();
        while (obstaclesCount-- > 0)
            generateObstacle(foodFrequency);
    }

    void generateLake(){
        int x = rand.nextInt(height);
        int y = rand.nextInt(width);
        int n=0, c=0;
        Queue<Pair> Q = new ConcurrentLinkedQueue<>();
        Q.add(new Pair(x, y));
        fields[x][y].visited=true;
        while(!Q.isEmpty() && n< freeArea && c<freeArea-6)
        {
            Pair p= Q.remove();
            int r=rand.nextInt(freeArea+4*n);
            if(r< freeArea) {
                if (!fieldAt(p).isWater)
                {
                    fieldAt(p).isWater = true;
                    c++;
                }
                n++;
                if(p.x>0){ fields[p.x-1][p.y].visited=true; Q.add(new Pair(p.x-1, p.y)); }
                if(p.x<height-1){ fields[p.x+1][p.y].visited=true; Q.add(new Pair(p.x+1, p.y)); }
                if(p.y<width-1){ fields[p.x][p.y+1].visited=true; Q.add(new Pair(p.x, p.y+1)); }
                if(p.y>0){ fields[p.x][p.y-1].visited=true; Q.add(new Pair(p.x, p.y-1)); }
            }
        }
        freeArea=freeArea-c;
    }
    void generateObstacle(int f){
        boolean success = false;
        while (!success) {
            int x = rand.nextInt(height);
            int y = rand.nextInt(width);
            if (fields[x][y].isFree()) {
                if(!fields[x][y].isWater){
                    int a = rand.nextInt(100);
                    if (a<(f-1)*f){
                        fields[x][y].tree=true;
                        if(x>0) fields[x-1][y].foodFrequency /= 2;
                        if(x<height-1) fields[x+1][y].foodFrequency /= 2;
                        if(y>0) fields[x][y-1].foodFrequency /= 2;
                        if(y<width-1) fields[x][y+1].foodFrequency /= 2;
                        break;
                    }
                }
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
                if (fields[x][y].isFree(species)) {
                    fields[x][y].animal = new Animal(species);
                    for(int i = 0; i < Animal.GENECOUNT; i++)
                        starterGeneStats[i] += fields[x][y].animal.genes[i];
                    species.animalList.add(new Pair(x, y));
                    success = true;
                }
            }
        }
    }

    Field fieldAt(Pair p) {
        return fields[p.x][p.y];
    }

    Collection<Pair> findFood(Pair p, int d, Species species) {
        // returns list of all points q such that
        // - distance between q and p equals d
        // - there is food on this field
        List<Pair> list = new LinkedList<>();
        int x = p.x; int y = p.y;
        for (int i = Math.max(0, x - d); i <= Math.min(height - 1, x + d); i++) {
            int j = y - d + abs(i - x);
            if (j >= 0 && fields[i][j].food && (!fields[i][j].isWater || species.canSwim))
                list.add(new Pair(i, j));
            j = y + d - abs(i - x);
            if (j < width && fields[i][j].food && (!fields[i][j].isWater || species.canSwim))
                list.add(new Pair(i, j));
        }
        if(species.carrionFeeder)
        {
            for (int i = Math.max(0, x - d); i <= Math.min(height - 1, x + d); i++) {
                int j = y - d + abs(i - x);
                if (j >= 0 && fields[i][j].carrion>0 && (!fields[i][j].isWater || species.canSwim))
                    list.add(new Pair(i, j));
                j = y + d - abs(i - x);
                if (j < width && fields[i][j].carrion>0 && (!fields[i][j].isWater || species.canSwim))
                    list.add(new Pair(i, j));
            }
        }
        return list;
    }

    public void move(Pair p, Pair q) {
        // describes movement of an animal from location p to q
        Field P = fieldAt(p);
        Field Q = fieldAt(q);
        Pair copyP = new Pair(p);
        Animal a = P.animal;
        if (Q.food) {
            a.hunger += 50;
            Q.food = false;
        }
        if(Q.carrion>0 && a.species.carrionFeeder){
            a.hunger += 50;
            Q.carrion=0;
        }
        Q.animal = a;
        P.animal = null;
        p.x = q.x;
        p.y = q.y;
        if (a.hunger > 80 && !a.isYoung()) {
            // lays an egg with probability fertility%
            if (rand.nextInt(100) < a.genes[Animal.FertilityId]) {
                P.animal = a.getDescendant();
                a.species.descendantsList.add(copyP);
            }
        }
    }

    public boolean goUp(Pair p, Species species) {
        Pair q = new Pair(p.x - 1, p.y);
        if (q.x >= 0 && fieldAt(q).isFree(species)) {
            move(p, q);
            return true;
        }
        return false;
    }
    public boolean goDown(Pair p, Species species) {
        Pair q = new Pair(p.x + 1, p.y);
        if (q.x < height && fieldAt(q).isFree(species)) {
            move(p, q);
            return true;
        }
        return false;
    }
    public boolean goLeft(Pair p, Species species) {
        Pair q = new Pair(p.x, p.y - 1);
        if (q.y >= 0 && fieldAt(q).isFree(species)) {
            move(p, q);
            return true;
        }
        return false;
    }
    public boolean goRight(Pair p, Species species) {
        Pair q = new Pair(p.x, p.y + 1);
        if (q.y < width && fieldAt(q).isFree(species)) {
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
            for(int i = 0; i < Animal.GENECOUNT; i++) {
                species.geneSpeciesSum[i] = 0;
            }
            for (Iterator<Pair> it = species.animalList.iterator(); it.hasNext(); ) {
                Pair p = it.next();
                Animal a = fieldAt(p).animal;
                if (!a.step()) {
                    fieldAt(p).animal = null;
                    fieldAt(p).carrion=1;
                    it.remove();
                    continue;
                }
                for(int i = 0; i < Animal.GENECOUNT; i++) {
                    species.geneSpeciesSum[i] += a.genes[i];
                }
                animalCount++;
                if (a.isEgg()) {
                    continue;
                }
                if (!a.isEgg()) {
                    boolean moved = false;
                    for (int d = 1; d < a.genes[Animal.SightId] && !moved; d++)
                        for (Pair q : findFood(p, d, a.species)) {
                            if (q.x < p.x && goUp(p, a.species)) {
                                a.direction = Animal.UP;
                                moved = true;
                                break;
                            }
                            if (q.x > p.x && goDown(p, a.species)) {
                                a.direction = Animal.DOWN;
                                moved = true;
                                break;
                            }
                            if (q.y > p.y && goRight(p, a.species)) {
                                a.direction = Animal.RIGHT;
                                moved = true;
                                break;
                            }
                            if (q.y < p.y && goLeft(p, a.species)) {
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
                                    if (goUp(p, a.species)) {
                                        moved = true;
                                        a.direction = Animal.UP;
                                    }
                                    break;
                                case Animal.DOWN:
                                    if (goDown(p, a.species)) {
                                        moved = true;
                                        a.direction = Animal.DOWN;
                                    }
                                    break;
                                case Animal.LEFT:
                                    if (goLeft(p, a.species)) {
                                        moved = true;
                                        a.direction = Animal.LEFT;
                                    }
                                    break;
                                case Animal.RIGHT:
                                    if (goRight(p, a.species)) {
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

        float[] geneSum = new float[Animal.GENECOUNT];
        for(Species s : speciesList) {
            for(int i = 0; i < Animal.GENECOUNT; i++) {
                geneSum[i] += s.geneSpeciesSum[i];
            }
        }
        if(animalCount!=0) {
            for(int i = 0; i < Animal.GENECOUNT; i++) {
                avgGeneValue[i] = geneSum[i] / animalCount;
            }
        }

        stepCount++;

        return animalCount>0;
    }
}
