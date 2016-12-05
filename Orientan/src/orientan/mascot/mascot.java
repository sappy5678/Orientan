/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.mascot;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import orientan.config.Action;
import orientan.config.loadconfig;
import orientan.mascotEnvironment.mascotenvironment;

/**
 *
 * @author zp
 */
public class mascot {

    private TimelineManger animationManger = new TimelineManger();
    private loadconfig configList;
    private Stage mascotStage = new Stage();
    private ImageView MascotimageView = new ImageView(new Image(new File(System.getProperty("user.dir") + "\\img\\shime1.png").toURI().toString()));
    private Walk walkAction;
    private FallingAndBouncing fallAction;
    private Drag dragAction;
    private double oldX = 0;
    private double oldY = 0;
    private double newX = 0;
    private double newY = 0;
    private double mouseSpeedX = 0;
    private double mouseSpeedY = 0;
    boolean speedXIsPositive = true;
    boolean speedYIsPositive = true;
    private double mascotdeltaX = 0.5;
    private double mascotdeltaY = 0.02;
    //private Time currentTime;
    private long lastTime;

    public mascot(loadconfig actionConfig) {
        //初始化設定
        this.configList = actionConfig;
        //設定視窗初始位置
        mascotStage.setY(mascotenvironment.getFloor());
        mascotStage.setX(mascotenvironment.getRightWall() - 10);
        walkAction = new Walk(mascotStage, MascotimageView, configList.getData("Walk", "Move"), animationManger);
        fallAction = new FallingAndBouncing(mascotStage, MascotimageView, configList.getData("Falling", "Move"), animationManger);
        dragAction = new Drag(mascotStage, MascotimageView, configList.getData("Resisting", "Embedded"), animationManger);
        walkAction.play();
        //deltaX=Walk.getAnimation().get(0).getVelocity();
        /*按鈕測試
        javafx.scene.control.Button btn = new javafx.scene.control.Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

            @Override
            public void handle(javafx.event.ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
         */
 /**/
        //設定透明顯示視窗
        //stage透明化   
        mascotStage.setAlwaysOnTop(true);   //讓其永遠在最上層
        mascotStage.initStyle(StageStyle.TRANSPARENT);
        AnchorPane root = new AnchorPane();
        //設定root顏色
        //root.getChildren().add(btn);
        root.setStyle("-fx-background:transparent;");
        //設定scene顏色與大小
        Scene scene = new Scene(root, mascotenvironment.getImageWidth(), mascotenvironment.getImageHeight());
        scene.setFill(null);

        mascotStage.setScene(scene);
        mascotStage.show();
        //設定拖曳圖片

        scene.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                /* drag was detected, start drag-and-drop gesture*/
                //System.out.println("onDragDetected");
                /* allow MOVE transfer mode */
                animationManger.StopAll();

                if (me.getButton() != MouseButton.MIDDLE && me.getButton() != MouseButton.SECONDARY) {
                    //現在的滑鼠位置
                    //oldX=me.getSceneX();
                    //oldY=me.getSceneY();
                    //initialX = me.getSceneX();
                    //initialY = me.getSceneY();
                }
                lastTime = System.currentTimeMillis();
                me.consume();
            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                /* data is dragged over the target */
                double speedPowTwo = 0;
                if (mouseEvent.getButton() != MouseButton.MIDDLE && mouseEvent.getButton() != MouseButton.SECONDARY) {
                    //System.out.println("onDragOver");

                    if (System.currentTimeMillis() - lastTime >= 10) {
                        lastTime = System.currentTimeMillis();
                        newX = mouseEvent.getScreenX();
                        newY = mouseEvent.getScreenY();
                        if (!(newX - oldX == 0)) {
                            //speedPowTwo=Math.pow(newX-oldX,2.0)+Math.pow(newY-oldY, 2.0);
                            speedPowTwo = Math.pow(newX - oldX, 2.0);
                            if (newX - oldX >= 0) {
                                speedXIsPositive = true;
                            } else {
                                speedXIsPositive = false;
                            }
                            mouseSpeedX = Math.sqrt(Math.abs(speedPowTwo));
                            if (!speedXIsPositive) {
                                mouseSpeedX = mouseSpeedX * -1;
                            }
                            //System.out.println(mouseSpeed);
                        }
                        if (!(newY - oldY == 0)) {
                            //speedPowTwo=Math.pow(newX-oldX,2.0)+Math.pow(newY-oldY, 2.0);
                            speedPowTwo = Math.pow(newY - oldY, 2.0);
                            if (newY - oldY >= 0) {
                                speedYIsPositive = true;
                            } else {
                                speedYIsPositive = false;
                            }
                            mouseSpeedY = Math.sqrt(Math.abs(speedPowTwo));
                            if (!speedYIsPositive) {
                                mouseSpeedY = mouseSpeedY * -1;
                            }
                            //System.out.println(mouseSpeed);
                        }
                    }
                    dragAction.ResistingAndDrag(mouseSpeedX);
                    //System.out.print("X:");
                    //System.out.println(mouseSpeedX);
                    //System.out.print("Y:");
                    //System.out.println(mouseSpeedY);
                    oldX = newX;
                    oldY = newY;
                    mascotStage.setX(mouseEvent.getScreenX() - mascotenvironment.getImageWidth() / 2);
                    mascotStage.setY(mouseEvent.getScreenY() - mascotenvironment.getImageHeight() / 5);
                }
                mouseEvent.consume();
            }

        });
        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEventdrop) {
                double deltaY = mascotdeltaY;
                double deltaX = mascotdeltaX;
                animationManger.StopAll();
                fallAction.Falling(mouseSpeedX, mouseSpeedY);
            }
        });
        root.getChildren().add(MascotimageView);

    }

}
