package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;


public class BoardView {
    static Image[] animal_img = new Image[4];
    static {
        animal_img[Animal.UP] = new Image(String.valueOf(BoardView.class.getResource("img/up.png")));
        animal_img[Animal.DOWN] = new Image(String.valueOf(BoardView.class.getResource("img/down.png")));
        animal_img[Animal.LEFT] = new Image(String.valueOf(BoardView.class.getResource("img/left.png")));
        animal_img[Animal.RIGHT] = new Image(String.valueOf(BoardView.class.getResource("img/right.png")));
    }
    static Image food_img = new Image(String.valueOf(BoardView.class.getResource("img/leaf.png")));
    static Image obstacle_img = new Image(String.valueOf(BoardView.class.getResource("img/obstacle.png")));
    static int M = 40;
    Board b;
    @FXML
    public Canvas canvas;
    @FXML
    public Button StartButton;
    @FXML
    public Button PauseButton;
    @FXML
    public Button StopButton;
    private Timeline timeline;
    public void init (Board b){
        this.b=b;
        this.timeline=new Timeline(new KeyFrame(Duration.millis(200), a->{
            if(b.makeStep())
                draw();
            else {
                drawEnd();
                timeline.stop();
            }
        }));
        this.canvas.setWidth(b.width*M);
        this.canvas.setHeight(b.height * M);
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        StartButton.setOnAction(actionEvent -> this.timeline.play());
        PauseButton.setOnAction(actionEvent -> this.timeline.stop());
        StopButton.setOnAction(actionEvent -> {
            this.timeline.stop();
            drawEnd();
        });
    }

    public void draw()
    {
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        for(int i=0;i<b.height; i++)
        {
            for(int j=0; j<b.width; j++)
            {
                gc.setFill(Color.YELLOWGREEN);
                gc.fillRect(M*j, M*i, M, M);
                if(b.fields[i][j].animal != null) {
                    drawAnimal(b.fields[i][j].animal, M*j, M*i);
                    //gc.drawImage(animal_img[b.fields[i][j].animal.direction], M*j, M*i);
                }
                else if(b.fields[i][j].hasFood) {
                    gc.drawImage(food_img, M*j, M*i);
                }
                if(b.fields[i][j].obstacle) {
                    gc.drawImage(obstacle_img, M*j, M*i);
                }
            }
        }
    }

    public void drawAnimal(Animal a, int x, int y) {
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        if(a.isEgg())
            gc.fillOval(x, y, 15, 20);
        else
            gc.drawImage(animal_img[a.direction], x, y);
    }

    public void drawEnd() {
        Label endgame = new Label("Simulation ended after " + this.b.stepCount + " steps \n");
        Label counter = new Label (this.b.animalList.size() + " animals left");
    }
}
