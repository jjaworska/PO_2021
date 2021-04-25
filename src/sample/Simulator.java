package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;

public class Simulator {
    private Timeline timeline;
    private final BoardView boardview;
    public Simulator(BoardView boardview)
    {
        this.boardview=boardview;
        this.boardview.StartButton.setOnAction(actionEvent -> this.timeline.play());
        this.boardview.PauseButton.setOnAction(actionEvent -> this.timeline.stop());
        this.boardview.StopButton.setOnAction(actionEvent -> {
            this.timeline.stop();
            this.boardview.drawEnd();
        });
        this.timeline=new Timeline(new KeyFrame(Duration.millis(200), this::step));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }
    private void step(ActionEvent actionEvent)
    {
        if(this.boardview.b.makeStep())
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
