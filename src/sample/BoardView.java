package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class BoardView extends VBox {
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
        this.canvas=new Canvas(b.width*20, b.height*20);
        this.scrollpane=new ScrollPane(this.canvas);
        scrollpane.setPrefSize(Math.min(b.width*20, 400), Math.min(b.height*20, 400));
        if(b.width<=20) scrollpane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        if(b.height<=20) scrollpane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
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
                if(b.fields[i][j].obstacle)
                    gc.setFill(Color.GRAY);
                else
                gc.setFill(Color.GREEN);
                gc.fillRect(20*j, 20*i, 20, 20);
                if(b.fields[i][j].anim!=null)
                {
                    gc.setFill(Color.SADDLEBROWN);
                    gc.fillRect(20*j+4, 20*i+4, 12, 12);
                }
                else if(b.fields[i][j].has_food)
                {
                    gc.setFill(Color.TOMATO);
                    gc.fillOval(20*j+5, 20*i+5, 10, 10);
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
