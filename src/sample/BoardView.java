package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class BoardView extends VBox {
    board b;
    public Canvas canvas;
    public BoardView(board b)
    {
        this.b=b;
        this.canvas=new Canvas(b.height*20, b.width*20);
        this.getChildren().addAll(this.canvas);
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
                gc.fillRect(20*i, 20*j, 20, 20);
                if(b.fields[i][j].anim!=null)
                {
                    gc.setFill(Color.SADDLEBROWN);
                    gc.fillRect(20*i+5, 20*j+5, 10, 10);
                }
                else if(b.fields[i][j].has_food)
                {
                    gc.setFill(Color.TOMATO);
                    gc.fillOval(20*i+5, 20*j+5, 10, 10);
                }
            }
        }
    }
}
