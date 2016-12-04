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
public class Fall {

    private double deltaX = 0;
    private double deltaY = 0;
    private double falldeltaX = 0;
    private double falldeltaY = 0;
    private double oldX = 0;
    private double oldY = 0;
    private Timeline timeline = new Timeline();
    private Stage mascotStage;
    private ImageView MascotimageView;
    private Action Config;
    private Image fallImage=new Image(new File(System.getProperty("user.dir") + "\\img" + "/shime4.png").toURI().toString());

    public Fall(Stage InmascotStage, ImageView InMascotimageView, Action InConfig, TimelineManger InanimationManger) {
        this.mascotStage = InmascotStage;
        this.MascotimageView = InMascotimageView;
        this.Config = InConfig;
        deltaX = 0.2;
        deltaY = 0.25;
        falldeltaX = deltaX;
        falldeltaY = deltaY;
        //事件監聽  
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            double deltaY = falldeltaY;
            double deltaX = falldeltaX;

            public void handle(ActionEvent t) {

                //y的部分
                if (mascotStage.getY() + falldeltaY + oldY >= mascotenvironment.getFloor()) {
                    mascotStage.setY(mascotenvironment.getFloor());
                } else {
                    //if (oldY - deltaY >= 0) {
                    //   oldY = 0;
                    //} else 
                    if(Math.abs(oldY)-falldeltaY<0)
                    {
                        oldY=0;
                    }
                    else if (oldY < 0) {
                        oldY = oldY + falldeltaY;
                    }
                    
                    System.out.println(mascotStage.getY());
                    if(mascotStage.getY() + falldeltaY + oldY/5<=mascotenvironment.getCeiling())
                    {
                        mascotStage.setY(mascotenvironment.getCeiling());
                        oldY=0;
                    }
                    else
                        mascotStage.setY(mascotStage.getY() + falldeltaY + oldY/5);
                    System.out.println(mascotStage.getY());
                    //mascotStage.setY(mascotStage.getY() + deltaY );
                    falldeltaY += 0.02;
                    //System.out.println(deltaY);
                }
                //x的部分
                if (oldX != 0) {
                    
                    if (Math.abs(oldX) - falldeltaX <= 0) {
                        oldX = 0;
                    } else if (oldX > 0) {
                        oldX = oldX - falldeltaX;
                    } else if (oldX < 0) {
                        oldX = oldX + falldeltaX;
                    }
                    if(mascotStage.getX()+oldX<=mascotenvironment.getLeftWall())
                    {
                        mascotStage.setX(mascotenvironment.getLeftWall());
                        oldX=oldX*-1;
                    }             
                    else if(mascotStage.getX()+oldX>=mascotenvironment.getRightWall())
                    {
                        mascotStage.setX(mascotenvironment.getRightWall());
                        oldX=oldX*-1;
                    }
                    else
                        mascotStage.setX(mascotStage.getX()+oldX/5);
                }
                if (mascotStage.getY() >= mascotenvironment.getFloor()&&oldY==0) {
                    falldeltaY = deltaY;
                    timeline.stop();
                }
                else if(mascotStage.getY() >= mascotenvironment.getFloor()&&oldY!=0)
                {
                    
                    oldY=oldY*-1;
                }

            }
        };

        timeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, onFinished,new KeyValue(InMascotimageView.imageProperty(), fallImage)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(10),new KeyValue(InMascotimageView.imageProperty(), fallImage)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        InanimationManger.getTimelineList().add(timeline);

    }

    public void Falling(double InoldX, double InoldY) {
        oldX = InoldX;
        oldY = InoldY;
        falldeltaY = deltaY;
        timeline.play();
    }
}
