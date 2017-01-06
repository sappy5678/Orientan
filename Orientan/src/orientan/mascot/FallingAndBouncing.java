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
import orientan.config.FallingData;
import orientan.mascotEnvironment.mascotenvironment;

/**
 *
 * @author zp
 */
public class FallingAndBouncing extends MascotAction {

    private double RegistanceY = 0; //y方向阻力
    private double RegistanceX = 0; //x方向阻力
    private double Gravity = 0;   //重力加速度
    private double falldeltaX = 0; //目前為止x速度變化
    private double falldeltaY = 0; //目前為止y速度變化
    private double initialVelocityX = 0; //初始速度x
    private double initialVelocityY = 0; //初始速度y
    private Timeline fallTimeline = new Timeline();
    private Timeline bouncingTimeline = new Timeline();
    private Stage mascotStage;
    private ImageView MascotimageView;
    private Action BouncingConfig;
    private double imageOnFloor = 10;
    private Image fallImage;
    private Image jumpImage;
    private Image BouncingImage;
    private Image FallOnFloorImage;
    private Image StandImage;
    private double fallRegistanceY = 0;
    private double ReboundCoefficientX = 0.5;
    //private String ActionMode;
    //private Boolean ClimbMode;

    private boolean noCeiling = false;
    private boolean isaction;
    private boolean isBouncing = true;

    public FallingAndBouncing(Stage InmascotStage, ImageView InMascotimageView, FallingData InFallConfig, Action BouncingConfig, Action JumpConfig, TimelineManger InanimationManger, boolean isAction, String imgPath) {
        this.isaction = isAction;
        this.mascotStage = InmascotStage;
        this.MascotimageView = InMascotimageView;
        this.BouncingConfig = BouncingConfig;
        this.imagePath = imgPath;
        //this.ActionMode = InActionMode;
        //this.ClimbMode = InClimbMode;
        StandImage = new Image(new File(imagePath + "/shime1.png").toURI().toString());
        fallImage = new Image(new File(imagePath + InFallConfig.getFallingAction().getAnimation().get(0).getImage()).toURI().toString());
        jumpImage = new Image(new File(imagePath + JumpConfig.getAnimation().get(0).getImage()).toURI().toString());
        BouncingImage = new Image(new File(imagePath + BouncingConfig.getAnimation().get(1).getImage()).toURI().toString());
        FallOnFloorImage = new Image(new File(imagePath + BouncingConfig.getAnimation().get(0).getImage()).toURI().toString());
        RegistanceX = InFallConfig.getRegistanceX();  //0.1
        RegistanceY = InFallConfig.getRegistanceY();  //0.1
        Gravity = InFallConfig.getGravity() / 10;  //0.25
        falldeltaX = RegistanceX;
        falldeltaY = Gravity;
        fallRegistanceY = RegistanceY;
        //事件監聽  
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                if (falldeltaY - initialVelocityY > 0) {
                    fallRegistanceY = Math.abs(RegistanceY);
                    MascotimageView.setImage(fallImage);

                } else if (falldeltaY - initialVelocityY < 0) {
                    fallRegistanceY = Math.abs(RegistanceY) * -1;
                    if (isBouncing) {
                        MascotimageView.setImage(BouncingImage);
                    } else {
                        MascotimageView.setImage(jumpImage);
                    }
                } else if (falldeltaY - initialVelocityY == 0) {
                    fallRegistanceY = 0;
                }
                //y的部分
                if (mascotStage.getY() + falldeltaY - initialVelocityY - fallRegistanceY >= mascotenvironment.getFloor() + imageOnFloor) {
                    mascotStage.setY(mascotenvironment.getFloor() + imageOnFloor);
                    //反彈
                    if (Math.abs(falldeltaY - initialVelocityY - fallRegistanceY) < 10) {
                        isBouncing = false;
                        initialVelocityY = 0;
                        initialVelocityX = 0;
                    } else {
                        MascotimageView.setImage(FallOnFloorImage);
                        isBouncing = true;
                        //System.out.println(falldeltaY-initialVelocityY+"!!");
                        initialVelocityY = (falldeltaY - initialVelocityY) * 0.5;
                        falldeltaY = Gravity;
                        /*
                        System.out.println(initialVelocityY);
                        System.out.println(falldeltaY);
                        System.out.println(falldeltaY-initialVelocityY);
                        System.out.println(falldeltaY - initialVelocityY - fallRegistanceY+"!!!");
                        System.out.println("???????????????????");*/
                    }
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

                    if (mascotStage.getY() + falldeltaY - initialVelocityY - fallRegistanceY <= mascotenvironment.getCeiling() && !noCeiling) {
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
                        //此註解是為了讓等於0的權利給是否繼續反彈的y方法上
                        //initialVelocityX = 0;
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
                if (mascotStage.getY() >= mascotenvironment.getFloor() + imageOnFloor && isBouncing == false) {
                    falldeltaY = Gravity;
                    fallTimeline.stop();
                    bouncingTimeline.play();
                    
                    //System.out.println(ActionMode);
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
                isaction = false;
                mascotStage.setY(mascotenvironment.getFloor());
            }
        };
        bouncingTimeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(InMascotimageView.imageProperty(), FallOnFloorImage)));
        bouncingTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(InMascotimageView.imageProperty(), BouncingImage)));
        bouncingTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(800), BouncingOnFinished, new KeyValue(InMascotimageView.imageProperty(), StandImage)));
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
        isaction = false;
    }

    public void setNoCeiling(boolean noCeiling) {
        this.noCeiling = noCeiling;
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
