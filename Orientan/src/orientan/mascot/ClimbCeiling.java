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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import orientan.config.Action;
import orientan.mascotEnvironment.mascotenvironment;

/**
 *
 * @author zp
 */
public class ClimbCeiling extends MascotAction {

    private Timeline timeline = new Timeline();
    private ArrayList<Image> image = new ArrayList<Image>();
    private double time = 0;
    private double duration = 0;
    private String imagePath;
    private int poseNumber = 0;
    private int size = 0;
    private int leftORight = 1;
    private String[] token;
    private ArrayList<Double> poseVelocity = new ArrayList<Double>();

    public ClimbCeiling(Stage mascotStage, ImageView MascotimageView, Action CimbCeilingConfig, TimelineManger animationManger, String imgPath) {
        this.imagePath = imgPath;

        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                if (mascotStage.getX() <= mascotenvironment.getLeftWall()) {
                    MascotimageView.setRotate(180);
                } else if (mascotStage.getX() >= mascotenvironment.getRightWall()) {
                    MascotimageView.setRotate(0);
                }
                if (MascotimageView.getRotate() == 0) {
                    leftORight = Math.abs(leftORight)*-1;
                } else if (MascotimageView.getRotate() == 180) {
                    leftORight = Math.abs(leftORight);
                }
                //System.out.println(MascotimageView.getRotate());
                mascotStage.setX(mascotStage.getX() + poseVelocity.get(poseNumber)*leftORight);
                mascotStage.setX(mascotStage.getX() + 5 * leftORight);
                poseNumber++;
                if (poseNumber == size) {
                    poseNumber = 0;
                }
            }
        };

        duration = CimbCeilingConfig.getAnimation().get(0).getDuration() / 10;
        for (int i = 0; i < CimbCeilingConfig.getAnimation().size(); i++) {
            //image.add(new Image(new File(System.getProperty("user.dir") + "\\img" + Walk.getAnimation().get(i).getImage()).toURI().toString()));
            image.add(new Image(new File(imagePath + CimbCeilingConfig.getAnimation().get(i).getImage()).toURI().toString()));
        }
        for (int i = 0; i < CimbCeilingConfig.getAnimation().size(); i++) {
            if (i == 0) {
                timeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, onFinished, new KeyValue(MascotimageView.imageProperty(), image.get(i))));
                continue;
            }
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(time = time + (double) CimbCeilingConfig.getAnimation().get(i - 1).getDuration() / 10), onFinished, new KeyValue(MascotimageView.imageProperty(), image.get(i))));
            if (i == CimbCeilingConfig.getAnimation().size() - 1) {
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(time = time + (double) CimbCeilingConfig.getAnimation().get(i).getDuration() / 10), onFinished, new KeyValue(MascotimageView.imageProperty(), image.get(i))));
                token = CimbCeilingConfig.getAnimation().get(i).getVelocity().split(",|\n");
            poseVelocity.add(Double.parseDouble(token[0]));
            }
            token = CimbCeilingConfig.getAnimation().get(i - 1).getVelocity().split(",|\n");
            poseVelocity.add(Double.parseDouble(token[0]));
        }
        this.size = CimbCeilingConfig.getAnimation().size();
        animationManger.getTimelineList().add(timeline);
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
