package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.util.Duration;

public class Simulator {
    private Timeline timeline;
    private BoardView boardview;
    public Simulator(BoardView boardview)
    {
        this.boardview=boardview;
        this.boardview.button.setOnAction(actionEvent -> {
            this.timeline.play();
        });
        this.timeline=new Timeline(new KeyFrame(Duration.millis(200), this::step));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }
    private void step(ActionEvent actionEvent)
    {
        if(this.boardview.b.make_step())
            this.boardview.draw();
        else {
            this.boardview.drawEnd();
            timeline.stop();
        }
    }
    /*public void start()
    {
        this.timeline.play();
    }*/
    public void stop()
    {
        this.timeline.stop();
    }
}
