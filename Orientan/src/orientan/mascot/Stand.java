/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.mascot;

import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import orientan.config.Action;

/**
 *
 * @author zp
 */
public class Stand extends MascotAction {

    private Timeline timeline = new Timeline();
    private Image standImage;
    private double duration;

    public Stand(Stage mascotStage, ImageView MascotimageView, Action sitConfig, TimelineManger animationManger, String imgPath) {
        this.imagePath = imgPath;
        standImage = new Image(new File(imagePath + sitConfig.getAnimation().get(0).getImage()).toURI().toString());
        duration = sitConfig.getAnimation().get(0).getDuration();
        timeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(MascotimageView.imageProperty(), standImage)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(duration / 10), new KeyValue(MascotimageView.imageProperty(), standImage)));
        animationManger.getTimelineList().add(timeline);
    }
    public void setToStand(ImageView MascotimageView)
    {
        MascotimageView.setImage(this.standImage);
    }
   @Override
    public void play() {
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @Override
    public void play(int circleTime) {
        timeline.setCycleCount(circleTime);
        timeline.play();
    }
}