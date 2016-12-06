/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.mascot;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
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
import orientan.mascotEnvironment.Mouse;
import orientan.mascotEnvironment.mascotenvironment;

/**
 *
 * @author zp
 */
public class mascot {

    private TimelineManger animationManger = new TimelineManger();
    private loadconfig configList;
    private Stage mascotStage = new Stage();
    private Image DefaultImage=new Image(new File(System.getProperty("user.dir") + "\\img\\shime1.png").toURI().toString());
    private ImageView MascotimageView = new ImageView(DefaultImage);
    private Walk walkAction;
    private Run runAction;
    private Dash dashAction;
    private FallingAndBouncing fallAction;
    private Drag dragAction;
    private ArrayList<MascotAction> actionList = new ArrayList<MascotAction>();
    private Random random=new Random();
    private boolean isAction=false;
    //private double mascotdeltaX = 0.5;
    //private double mascotdeltaY = 0.02;
    //private Time currentTime;

    public mascot(loadconfig actionConfig, Mouse mouseDetect) {
        //初始化設定
        this.configList = actionConfig;
        //設定視窗初始位置
        mascotStage.setY(mascotenvironment.getFloor());
        mascotStage.setX(mascotenvironment.getRightWall() - 10);
        walkAction = new Walk(mascotStage, MascotimageView, configList.getData("Walk", "Move"), animationManger);
        runAction= new Run(mascotStage, MascotimageView, configList.getData("Run", "Move"), animationManger);
        dashAction= new Dash(mascotStage, MascotimageView, configList.getData("Dash", "Move"), animationManger);
        fallAction = new FallingAndBouncing(mascotStage, MascotimageView, configList.getData("Falling", "Move"), animationManger,isAction);
        dragAction = new Drag(mascotStage, MascotimageView, configList.getData("Resisting", "Embedded"), animationManger);
        actionList.add(walkAction);
        actionList.add(runAction);
        actionList.add(dashAction);

             actionList.get(random.nextInt(3)).play(random.nextInt(20)+1);

       
        //System.out.println(configList.getData("Resisting", "Embedded").getAnimation().size());
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
        scene.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if(mascotStage.getY()==mascotenvironment.getFloor())
                    isAction=false;
                if(!isAction)
                {
                    animationManger.StopAll(); 
                    MascotimageView.setImage(DefaultImage);
                }          
                me.consume();
            }
        });
        scene.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if(mascotStage.getY()==mascotenvironment.getFloor())
                    isAction=false;
                if(!isAction)
                {
                     actionList.get(random.nextInt(3)).play(random.nextInt(20)+1);
                }            
                me.consume();
            }
        });
        scene.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                isAction=true;
                /* drag was detected, start drag-and-drop gesture*/
                //System.out.println("onDragDetected");
                /* allow MOVE transfer mode */
                if (me.getButton() != MouseButton.MIDDLE && me.getButton() != MouseButton.SECONDARY) {
                    animationManger.StopAll();  
                }
                me.consume();
            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                /* data is dragged over the target */
                if (mouseEvent.getButton() != MouseButton.MIDDLE && mouseEvent.getButton() != MouseButton.SECONDARY) {
                    mouseDetect.updateMouseData(mouseEvent);
                    dragAction.ResistingAndDrag(mouseDetect);
                    mascotStage.setX(mouseEvent.getScreenX() - mascotenvironment.getImageWidth() / 2);
                    mascotStage.setY(mouseEvent.getScreenY() - mascotenvironment.getImageHeight() / 5);
                }
                mouseEvent.consume();
            }

        });

        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEventdrop) {
                if (mouseEventdrop.getButton() != MouseButton.MIDDLE && mouseEventdrop.getButton() != MouseButton.SECONDARY) {
                    animationManger.StopAll();
                    fallAction.Falling(mouseDetect.getMouseSpeedX(), mouseDetect.getMouseSpeedY());
                    //isAction=false;
                }
            }
        });
        root.getChildren().add(MascotimageView);

    }

}
