package sample;

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


public class BoardView extends VBox {
    static Image[] animal_img = new Image[4];
    static {
        animal_img[animal.UP] = new Image(String.valueOf(BoardView.class.getResource("img/up.png")));
        animal_img[animal.DOWN] = new Image(String.valueOf(BoardView.class.getResource("img/down.png")));
        animal_img[animal.LEFT] = new Image(String.valueOf(BoardView.class.getResource("img/left.png")));
        animal_img[animal.RIGHT] = new Image(String.valueOf(BoardView.class.getResource("img/right.png")));
    }
    static Image food_img = new Image(String.valueOf(BoardView.class.getResource("img/leaf.png")));
    static Image obstacle_img = new Image(String.valueOf(BoardView.class.getResource("img/obstacle.png")));
    static int M = 40;
    board b;
    public Canvas canvas;
    public ScrollPane scrollpane;
    public HBox buttons;
    public Button StartButton;
    public Button PauseButton;
    public Button StopButton;
    public BoardView(board b)
    {
        this.b=b;
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20, 20, 20, 20));
        setSpacing(20);
        this.buttons=new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        this.StartButton=new Button("Start!");
        this.PauseButton=new Button("Pause");
        this.StopButton=new Button("Stop");
        this.canvas=new Canvas(b.width*M, b.height*M);
        this.scrollpane=new ScrollPane(this.canvas);
        scrollpane.setPrefSize(Math.min(b.width*M, 400), Math.min(b.height*M, 400));
        if(b.height<=10) scrollpane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        if(b.width<=10) scrollpane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        buttons.getChildren().addAll(this.StartButton, this.PauseButton, this.StopButton);

        this.getChildren().addAll(this.scrollpane, this.buttons);
    }
    public void draw()
    {
        GraphicsContext gc=this.canvas.getGraphicsContext2D();
        for(int i=0;i<b.height; i++)
        {
            for(int j=0; j<b.width; j++)
            {
                gc.setFill(Color.PALEGREEN);
                gc.fillRect(M*j, M*i, M, M);
                if(b.fields[i][j].anim != null) {
                    gc.drawImage(animal_img[b.fields[i][j].anim.direction], M*j, M*i);
                }
                else if(b.fields[i][j].has_food) {
                    gc.drawImage(food_img, M*j, M*i);
                }
                if(b.fields[i][j].obstacle) {
                    /*gc.setFill(Color.GREY);
                    gc.fillRect(M*j, M*i, M, M);*/
                    gc.drawImage(obstacle_img, M*j, M*i);
                }
            }
        }
    }
    public void drawEnd() {
        Label endgame = new Label("Simulation ended after " + this.b.stepCount + " steps \n");
        Label counter = new Label (this.b.animal_list.size() + " animals left");
        this.getChildren().clear();
        this.getChildren().addAll(endgame, counter);
    }
}
