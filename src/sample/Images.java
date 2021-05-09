package sample;

import javafx.scene.image.Image;

public class Images {
    static Image [] t = new Image[2];
    static{
        t[0] = new Image(String.valueOf(BoardView.class.getResource("img/UP.png")));
        t[1] = new Image(String.valueOf(BoardView.class.getResource("img/wersja1.png")));
    }
}
