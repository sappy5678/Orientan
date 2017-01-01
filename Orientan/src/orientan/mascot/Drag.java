/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.mascot;

import java.io.File;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import orientan.config.Action;
import orientan.mascotEnvironment.Mouse;

/**
 *
 * @author zp
 */
class Drag extends MascotAction {

    private ArrayList<Image> image = new ArrayList<Image>();
    private Timeline resistingTimeline = new Timeline();
    private boolean mouseSpeedXIsPositive = true;
    private ImageView imageView;
    private Image pinchedLeft3;
    private Image pinchedRight3;
    private Image pinchedLeft2;
    private Image pinchedRight2;
    private Image pinchedLeft1;
    private Image pinchedRight1;
    private Image pinchedCenter;

    private TimelineManger timeLineManger;

    public Drag(Stage mascotStage, ImageView MascotimageView, Action Config, TimelineManger animationManger, String imgPath) {
        this.timeLineManger = animationManger;
        this.imagePath = imgPath;
        pinchedLeft2 = new Image(new File(imagePath + "/shime7.png").toURI().toString());
        pinchedRight2 = new Image(new File(imagePath + "/shime8.png").toURI().toString());
        pinchedLeft1 = new Image(new File(imagePath + "/shime5.png").toURI().toString());
        pinchedRight1 = new Image(new File(imagePath + "/shime6.png").toURI().toString());
        pinchedCenter = new Image(new File(imagePath + "/shime1.png").toURI().toString());
        /*
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(MascotimageView.imageProperty(), new Image(new File(System.getProperty("user.dir") + "\\img" + "/shime18.png").toURI().toString()))));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(MascotimageView.imageProperty(), new Image(new File(System.getProperty("user.dir") + "\\img" + "/shime19.png").toURI().toString()))));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(800), BouncingOnFinished,new KeyValue(MascotimageView.imageProperty(), new Image(new File(System.getProperty("user.dir") + "\\img" + "/shime1.png").toURI().toString()))));
        resistingTimeline.setCycleCount(1);
        animationManger.getTimelineList().add(resistingTimeline);*/

        this.imageView = MascotimageView;

        /*
        for (int i = 0; i < Config.getAnimation().size(); i++) {
            //image.add(new Image(new File(System.getProperty("user.dir") + "\\img" + Walk.getAnimation().get(i).getImage()).toURI().toString()));
            image.add(new Image(new File(System.getProperty("user.dir") + "\\img" + Config.getAnimation().get(i).getImage()).toURI().toString()));
        }
        for (int i = 0; i < Config.getAnimation().size(); i++) {
            if (i == 0) {
                resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(MascotimageView.imageProperty(), image.get(i))));
                continue;
            }
            resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds((double) Config.getAnimation().get(i - 1).getDuration() / 10), new KeyValue(MascotimageView.imageProperty(), image.get(i))));
            if (i == Config.getAnimation().size() - 1) {
                resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds((double) Config.getAnimation().get(i).getDuration() / 10), new KeyValue(MascotimageView.imageProperty(), image.get(i))));
            }
        }
         */
 /*
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2), new KeyValue(MascotimageView.imageProperty(), pinchedCenter)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(7), new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(7.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(8), new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(8.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(9), new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(9.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(10), new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(10.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(11), new KeyValue(MascotimageView.imageProperty(), pinchedCenter)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(21), new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(21.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(22), new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(22.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(23), new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(23.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(24), new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(24.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(25), new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(25.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(26), new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(26.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(27), new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(27.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(28), new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(28.5), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
         */
        resistingTimeline.getKeyFrames()
                .add(new KeyFrame(Duration.ZERO, new KeyValue(MascotimageView.imageProperty(), pinchedLeft1)));
        resistingTimeline.getKeyFrames()
                .add(new KeyFrame(Duration.seconds(0.5), new KeyValue(MascotimageView.imageProperty(), pinchedCenter)));
        resistingTimeline.getKeyFrames()
                .add(new KeyFrame(Duration.seconds(1), new KeyValue(MascotimageView.imageProperty(), pinchedRight1)));
        resistingTimeline.getKeyFrames()
                .add(new KeyFrame(Duration.seconds(1.5), new KeyValue(MascotimageView.imageProperty(), pinchedCenter)));
        resistingTimeline.getKeyFrames()
                .add(new KeyFrame(Duration.seconds(2), new KeyValue(MascotimageView.imageProperty(), pinchedCenter)));
        resistingTimeline.setCycleCount(Timeline.INDEFINITE);

        animationManger.getTimelineList()
                .add(resistingTimeline);
    }

    public void ResistingAndDrag(Mouse mouseDetect) {
        imageView.setRotationAxis(Rotate.Y_AXIS);
        imageView.setRotate(0);
        if (mouseDetect.getMouseSpeedX() >= 0) {
            this.mouseSpeedXIsPositive = true;

        } else {
            this.mouseSpeedXIsPositive = false;
        }
        if (Math.abs(mouseDetect.getMouseSpeedX()) <= 3) {
            imageView.setImage(pinchedCenter);
            resistingTimeline.play();
        } else {
            timeLineManger.StopAll();
            if (Math.abs(mouseDetect.getMouseSpeedX()) >= 30) {
                if (mouseSpeedXIsPositive) {
                    imageView.setImage(pinchedLeft3);
                } else {
                    imageView.setImage(pinchedRight3);
                }
            } else if ((Math.abs(mouseDetect.getMouseSpeedX()) < 30 && Math.abs(mouseDetect.getMouseSpeedX()) >= 10)) {
                if (mouseSpeedXIsPositive) {
                    imageView.setImage(pinchedLeft2);
                } else {
                    imageView.setImage(pinchedRight2);
                }
            } else if (Math.abs(mouseDetect.getMouseSpeedX()) < 10 && Math.abs(mouseDetect.getMouseSpeedX()) > 3) {
                if (mouseSpeedXIsPositive) {
                    imageView.setImage(pinchedLeft1);
                } else {
                    imageView.setImage(pinchedRight1);
                }
            }
        }

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
