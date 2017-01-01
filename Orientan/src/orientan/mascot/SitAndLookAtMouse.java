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
import orientan.mascotEnvironment.Mouse;

/**
 *
 * @author user
 */
public class SitAndLookAtMouse extends MascotAction {

    private Timeline timeline = new Timeline();
    private Image sitImage;
    private double duration;
    private Mouse mouseDetect;

    public SitAndLookAtMouse(Stage mascotStage, ImageView MascotimageView, Action SitAndLookAtMouseConfig, TimelineManger animationManger, Mouse mouseDetect, String imgPath) {
        this.imagePath = imgPath;
        //未完成
        sitImage = new Image(new File(imagePath+ SitAndLookAtMouseConfig.getAnimation().get(0).getImage()).toURI().toString());
        duration = SitAndLookAtMouseConfig.getAnimation().get(0).getDuration();
        timeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(MascotimageView.imageProperty(), sitImage)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(duration / 10), new KeyValue(MascotimageView.imageProperty(), sitImage)));
        timeline.setCycleCount(1);
    }

    @Override
    public void play() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void play(int circleTime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
