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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import orientan.config.Action;
import orientan.mascotEnvironment.mascotenvironment;

/**
 *
 * @author zp
 */
public class FallingAndBouncing {

    private double RegistanceY = 0;
    private double RegistanceX = 0;
    private double Gravity = 0;
    private double falldeltaX = 0;
    private double falldeltaY = 0;
    private double initialVelocityX = 0;
    private double initialVelocityY = 0;
    private Timeline fallTimeline = new Timeline();
    private Timeline bouncingTimeline = new Timeline();
    private Stage mascotStage;
    private ImageView MascotimageView;
    private Action Config;
    private double imageOnFloor = 10;
    private Image fallImage = new Image(new File(System.getProperty("user.dir") + "\\img" + "/shime4.png").toURI().toString());
    private Image jumpImage = new Image(new File(System.getProperty("user.dir") + "\\img" + "/shime22.png").toURI().toString());
    private double fallRegistanceY = 0;
    private double ReboundCoefficientX = 0.5;
    private boolean isaction;
    public FallingAndBouncing(Stage InmascotStage, ImageView InMascotimageView, Action InConfig, TimelineManger InanimationManger,boolean isAction) {
        this.isaction=isAction;
        this.mascotStage = InmascotStage;
        this.MascotimageView = InMascotimageView;
        this.Config = InConfig;
        RegistanceX = 0.1;  //0.2
        RegistanceY = 0.1;
        Gravity = 0.25;  //0.25
        falldeltaX = RegistanceX;
        falldeltaY = Gravity;
        fallRegistanceY = RegistanceY;
        //事件監聽  
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                if (falldeltaY - initialVelocityY > 0) {
                    fallRegistanceY = Math.abs(fallRegistanceY);
                    MascotimageView.setImage(fallImage);

                } else if (falldeltaY - initialVelocityY < 0) {
                    fallRegistanceY = Math.abs(fallRegistanceY) * -1;
                    MascotimageView.setImage(jumpImage);
                } else if (falldeltaY - initialVelocityY == 0) {
                    fallRegistanceY = 0;
                }
                //y的部分
                if (mascotStage.getY() + falldeltaY - initialVelocityY - fallRegistanceY + imageOnFloor >= mascotenvironment.getFloor()) {
                    mascotStage.setY(mascotenvironment.getFloor() + imageOnFloor);
                    initialVelocityY = 0;
                } else {
                    //if (oldY - deltaY >= 0) {
                    //   oldY = 0;
                    //} else 
                    /*
                    if (Math.abs(initialVelocityY) - falldeltaY <= 0) {
                        initialVelocityY = 0;
                    } else if (initialVelocityY > 0) {
                        initialVelocityY = initialVelocityY + falldeltaY;
                    }
                     */
                    //System.out.println(mascotStage.getY());
                    //天花板限制
                    if (mascotStage.getY() + falldeltaY - initialVelocityY - fallRegistanceY <= mascotenvironment.getCeiling()) {
                        mascotStage.setY(mascotenvironment.getCeiling());
                        falldeltaY = Gravity;
                        initialVelocityY = 0;
                    } else {
                        mascotStage.setY(mascotStage.getY() + falldeltaY - initialVelocityY - fallRegistanceY);
                    }
                    //System.out.println(mascotStage.getY());
                    //mascotStage.setY(mascotStage.getY() + deltaY );
                    falldeltaY += Gravity;
                    //System.out.println(deltaY);
                }
                //x的部分
                if (initialVelocityX != 0) {

                    if (Math.abs(initialVelocityX) - falldeltaX <= 0) {
                        initialVelocityX = 0;
                    } else if (initialVelocityX > 0 && initialVelocityX != 0) {
                        initialVelocityX = initialVelocityX - falldeltaX;
                    } else if (initialVelocityX < 0 && initialVelocityX != 0) {
                        initialVelocityX = initialVelocityX + falldeltaX;
                    }
                    MascotimageView.setRotationAxis(Rotate.Y_AXIS);
                    if (initialVelocityX > 0) {
                        MascotimageView.setRotate(180);
                    } else if (initialVelocityX < 0) {
                        MascotimageView.setRotate(0);
                    }
                    if (mascotStage.getX() + initialVelocityX <= mascotenvironment.getLeftWall()) {
                        mascotStage.setX(mascotenvironment.getLeftWall());
                        initialVelocityX = initialVelocityX * ReboundCoefficientX * -1;
                        MascotimageView.setRotate(180);
                    } else if (mascotStage.getX() + initialVelocityX >= mascotenvironment.getRightWall()) {
                        mascotStage.setX(mascotenvironment.getRightWall());
                        initialVelocityX = initialVelocityX * ReboundCoefficientX * -1;
                        MascotimageView.setRotate(0);
                    } else {
                        mascotStage.setX(mascotStage.getX() + initialVelocityX);
                    }
                }
                //結束條件  目前註解掉的oldY部分是反彈
                if (mascotStage.getY() >= mascotenvironment.getFloor() + imageOnFloor/*&&oldY==0*/) {
                    falldeltaY = Gravity;
                    fallTimeline.stop();
                    bouncingTimeline.play();
                }/*
                else if(mascotStage.getY() >= mascotenvironment.getFloor()&&oldY!=0)
                {
                    
                    oldY=oldY*-0.5;
                }
                 */
            }
        };
        /*
        timeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, onFinished, new KeyValue(InMascotimageView.imageProperty(), fallImage)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(10), new KeyValue(InMascotimageView.imageProperty(), fallImage)));*/
        fallTimeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, onFinished));
        fallTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(10)));
        fallTimeline.setCycleCount(Timeline.INDEFINITE);
        InanimationManger.getTimelineList().add(fallTimeline);

        //事件監聽  
        EventHandler BouncingOnFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                isaction=false;
                mascotStage.setY(mascotenvironment.getFloor());
            }
        };
        bouncingTimeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(InMascotimageView.imageProperty(), new Image(new File(System.getProperty("user.dir") + "\\img" + "/shime18.png").toURI().toString()))));
        bouncingTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(InMascotimageView.imageProperty(), new Image(new File(System.getProperty("user.dir") + "\\img" + "/shime19.png").toURI().toString()))));
        bouncingTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(800), BouncingOnFinished, new KeyValue(InMascotimageView.imageProperty(), new Image(new File(System.getProperty("user.dir") + "\\img" + "/shime1.png").toURI().toString()))));
        bouncingTimeline.setCycleCount(1);
        InanimationManger.getTimelineList().add(bouncingTimeline);
    }

    public void Falling(double InoldX, double InoldY) {

        initialVelocityX = (InoldX) / 3;
        //使之y軸往上為正
        initialVelocityY = (InoldY * -1) / 3;
        falldeltaY = Gravity;
        fallRegistanceY = RegistanceY;
        fallTimeline.play();
        isaction=false;
    }
}
