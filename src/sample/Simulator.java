package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;

public class Simulator {
    private Timeline timeline;
    private BoardView boardview;
    public Simulator(BoardView boardview)
    {
        this.boardview=boardview;
        this.timeline=new Timeline(new KeyFrame(Duration.millis(200), this::step));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }
    private void step(ActionEvent actionEvent)
    {
        this.boardview.b.make_step();
        this.boardview.draw();
        //if(this.boardview.b.animal_list.size() == 0)
            //this.timeline.stop();
    }
    public void start()
    {
        this.timeline.play();
    }
    public void stop()
    {
        this.timeline.stop();
    }
}
