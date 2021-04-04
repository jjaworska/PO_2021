package sample;

import java.util.*;

import static java.lang.Math.abs;

public class board {
    public static class pair {
        int x; int y;
        pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public static class field {
        animal anim;
        boolean has_food;
        field() {
            this.anim = null;
            has_food = false;
        }
        boolean is_free() {
            return anim == null;
        }
        @Override
        public String toString() {
            if(anim != null)
                return String.valueOf(anim.name);
            if(has_food)
                return "#";
            return ".";
        }
    }

    // fields of the board class
    int height;
    int width;
    field[][] fields;
    LinkedList<pair> animal_list;
    Random rand;

    board(int height, int width, int animal_count, int food_count) {
        this.height = height;
        this.width = width;
        this.rand = new Random();

        fields = new field[height][width];
        for (int x = 0; x < height; x++)
            for (int y = 0; y < width; y++)
                fields[x][y] = new field();

        animal_list = new LinkedList<>();
        while(animal_count-- > 0)
            generate_animal();

        while(food_count-- > 0)
            generate_food();

        print();
    }
    void generate_animal() {
        boolean success = false;
        while(!success) {
            int x = rand.nextInt(height);
            int y = rand.nextInt(width);
            if(fields[x][y].is_free()) {
                fields[x][y].anim = new animal();
                animal_list.add(new pair(x, y));
                success = true;
            }
        }
    }
    void generate_food() {
        boolean success = false;
        while(!success) {
            int x = rand.nextInt(height);
            int y = rand.nextInt(width);
            if(fields[x][y].is_free() && !fields[x][y].has_food) {
                fields[x][y].has_food = true;
                success = true;
            }
        }
    }

    void print() {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++)
                System.out.print(fields[x][y].toString());
            System.out.println();
        }
        System.out.println("");
    }

    field field_at(pair p) {
        return fields[p.x][p.y];
    }

    Collection<pair> neighbourhood(pair p, int d) {
        // returns list of all points q such that distance between p and q equals d
        List<pair> list = new LinkedList<>();
        int x = p.x; int y = p.y;
        for (int i = Math.max(0, x - d); i <= Math.min(height - 1, x + d); i++) {
            int j = y - d + abs(i - x);
            if (j >= 0 && fields[i][j].has_food)
                list.add(new pair(i, j));
            j = y + d - abs(i - x);
            if (j < width && fields[i][j].has_food)
                list.add(new pair(i, j));
        }
        return list;
    }

    public void move(pair p, pair q) {
        // describes movement from location p to q
        field P = field_at(p);
        field Q = field_at(q);
        animal a = P.anim;
        if(Q.has_food) {
            a.hunger = 100;
            Q.has_food = false;
            Q.anim = a;
            P.anim = null;
            p.x = q.x; p.y = q.y;
            generate_food();
        }
        else {
            Q.anim = a;
            P.anim = null;
            p.x = q.x; p.y = q.y;
        }
    }

    public boolean go_up(pair p) {
        pair q = new pair(p.x - 1, p.y);
        if(q.x >= 0 && field_at(q).is_free()) {
            move(p, q);
            return true;
        }
        return false;
    }
    public boolean go_down(pair p) {
        pair q = new pair(p.x + 1, p.y);
        if(q.x < height && field_at(q).is_free()) {
            move(p, q);
            return true;
        }
        return false;
    }
    public boolean go_left(pair p) {
        pair q = new pair(p.x, p.y - 1);
        if(q.y >= 0 && field_at(q).is_free()) {
            move(p, q);
            return true;
        }
        return false;
    }
    public boolean go_right(pair p) {
        pair q = new pair(p.x, p.y + 1);
        if(q.y < width && field_at(q).is_free()) {
            move(p, q);
            return true;
        }
        return false;
    }

    public void make_step() {
        for (Iterator<pair> it = animal_list.iterator(); it.hasNext();) {
            pair p = it.next();
            animal a = field_at(p).anim;
            if(a.step()) {
                boolean moved = false;
                for (int d = 1; d < a.sight && !moved; d++)
                    for (pair q : neighbourhood(p, d)) {
                        if(q.x < p.x && go_up(p)) {
                            moved = true;
                            break;
                        }
                        if(q.x > p.x && go_down(p)) {
                            moved = true;
                            break;
                        }
                        if(q.y > p.y && go_right(p)) {
                            moved = true;
                            break;
                        }
                        if(q.y < p.y && go_left(p)) {
                            moved = true;
                            break;
                        }
                    }
                if(!moved)
                    if(go_up(p) || go_down(p) || go_left(p) || go_right(p))
                        moved = true;
            }
            else {
                System.out.println("+1 death " + a.name);
                field_at(p).anim = null;
                it.remove();
            }
        }
    }

    public static void main(String[] args) {
        board test = new board(13, 30, 10, 10);
        int cnt = 0;
        while(test.animal_list.size() > 0) {
            test.make_step();
            test.print();
            cnt++;
        }
        System.out.println("Simulation ended after " + cnt + " steps");
    }
}
