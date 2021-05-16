package sample;

import javafx.scene.image.Image;

public class Images {
    static Image[] t = new Image[SpeciesView.imagesCount];

    static {
        t[0] = new Image(String.valueOf(BoardView.class.getResource("img/purple.png")));
        t[1] = new Image(String.valueOf(BoardView.class.getResource("img/blue.png")));
        t[2] = new Image(String.valueOf(BoardView.class.getResource("img/green.png")));
        t[3] = new Image(String.valueOf(BoardView.class.getResource("img/yellow.png")));
        t[4] = new Image(String.valueOf(BoardView.class.getResource("img/red.png")));
        t[5] = new Image(String.valueOf(BoardView.class.getResource("img/pink.png")));
    }
}
