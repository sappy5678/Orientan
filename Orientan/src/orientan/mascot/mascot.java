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
    private Fall fallAction;
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
        fallAction = new Fall(mascotStage, MascotimageView, configList.getData("Falling", "Move"), animationManger);
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
        Scene scene = new Scene(root,mascotenvironment.getImageWidth(),mascotenvironment.getImageHeight());
        scene.setFill(null);

        mascotStage.setScene(scene);
        mascotStage.show();

        scene.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                /* drag was detected, start drag-and-drop gesture*/
                System.out.println("onDragDetected");
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

                    if (System.currentTimeMillis()-lastTime  >= 10) {
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
                    System.out.print("X:");
                    System.out.println(mouseSpeedX);
                    System.out.print("Y:");
                    System.out.println(mouseSpeedY);
                    oldX = newX;
                    oldY = newY;
                    mascotStage.setX(mouseEvent.getScreenX() - mascotenvironment.getImageWidth() / 2);
                    mascotStage.setY(mouseEvent.getScreenY() - mascotenvironment.getImageHeight() / 5);
                    /* accept it only if it is  not dragged from the same node 
                 * and if it has a string data */
                    //mouseEvent.acceptTransferModes(TransferMode.MOVE);              

                }
                mouseEvent.consume();
            }

        });
        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEventdrop) {

                double deltaY = mascotdeltaY;
                double deltaX = mascotdeltaX;
                fallAction.Falling(mouseSpeedX, mouseSpeedY);
                /*
                System.out.println("aaa");
                int i=0;
                while (mascotStage.getY() < mascotenvironment.getFloor()) {
                    
                    System.out.println(i);
                    i++;
                    //y的部分
                    if (mascotStage.getY() + deltaY - mouseSpeedY >= mascotenvironment.getFloor()) {
                        mascotStage.setY(mascotenvironment.getFloor());
                    } 
                    else {
                        if (mouseSpeedY - deltaY <= 0) {
                            mouseSpeedY = 0;
                        } else if (mouseSpeedY > 0) {
                            mouseSpeedY = mouseSpeedY - deltaY;
                        }
                        System.out.println(mascotStage.getY());
                        mascotStage.setY(mascotStage.getY() + deltaY - mouseSpeedY);
                        //deltaY += 0.005;
                    }
                    try {
                        
                        //x的部分
                        if (oldX != 0) {
                        if (Math.abs(oldX) - deltaX <= 0) {
                        oldX = 0;
                        } else if (oldX > 0) {
                        oldX = oldX - deltaX;
                        } else if (oldX < 0) {
                        oldX = oldX + deltaX;
                        }
                        mascotStage.setX(mascotStage.getY() + oldX);
                        }
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(mascot.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }*/

            }

        });
        //配置動畫圖片  
        /*
        Image image1 = new Image(new File(System.getProperty("user.dir") + "\\img\\shime1.png").toURI().toString());
        Image image2 = new Image(new File(System.getProperty("user.dir") + "\\img\\shime2.png").toURI().toString());
        Image image3 = new Image(new File(System.getProperty("user.dir") + "\\img\\shime3.png").toURI().toString());
         */
        //取得螢幕框架
        //Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

        //事件監聽  
        /*
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //如果超出左右邊界，就變換前進方向與圖片翻轉
                //if(mascotStage.getX()<=-20||mascotStage.getX()>=primScreenBounds.getWidth())
                if (mascotStage.getX() <= primScreenBounds.getMinX() || mascotStage.getX() >= primScreenBounds.getWidth() - image1.getWidth()) {
                    deltaX = deltaX * (-1);
                    imageView.setRotationAxis(Rotate.Y_AXIS);
                    if (deltaX > 0) {
                        imageView.setRotate(180);
                    } else {
                        imageView.setRotate(0);
                    }
                }
                mascotStage.setX((mascotStage.getX() + deltaX));
            }
        };*/
        //設定時間軸
        /*
        KeyFrame start;
        KeyFrame end;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, onFinished, new KeyValue(imageView.imageProperty(), image1)),
                //new KeyFrame(Duration.seconds(0.5), new KeyValue(imageView.imageProperty(), image1)),
                new KeyFrame(Duration.seconds(0.4), onFinished, new KeyValue(imageView.imageProperty(), image2)),
                new KeyFrame(Duration.seconds(0.8), onFinished, new KeyValue(imageView.imageProperty(), image1)),
                new KeyFrame(Duration.seconds(1.2), onFinished, new KeyValue(imageView.imageProperty(), image3)),
                //下面這行是給不斷循環的時間軸在下一次循環的緩衝，如果刪掉的話，這個最後一個影格會因為太快而看不到
                new KeyFrame(Duration.seconds(1.6), new KeyValue(imageView.imageProperty(), image3))
        );*//*
        Timeline timeline = new Timeline();   
        ArrayList<Image> image = new ArrayList<Image>();
        double time = 0;
        double duration = Walk.getAnimation().get(0).getDuration() / 10;
        for (int i = 0; i < Walk.getAnimation().size(); i++) {
            //image.add(new Image(new File(System.getProperty("user.dir") + "\\img" + Walk.getAnimation().get(i).getImage()).toURI().toString()));
            image.add(new Image(new File(System.getProperty("user.dir") + "\\img" + Walk.getAnimation().get(i).getImage()).toURI().toString()));
        }
        for (int i = 0; i < Walk.getAnimation().size(); i++) {
            if (i == 0) {
                timeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, onFinished, new KeyValue(imageView.imageProperty(), image.get(i))));
                continue;
            }
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(time = time + (double)Walk.getAnimation().get(i-1).getDuration() / 10), onFinished, new KeyValue(imageView.imageProperty(), image.get(i))));
            if (i == Walk.getAnimation().size()-1) {
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(time = time + (double)Walk.getAnimation().get(i).getDuration() / 10), new KeyValue(imageView.imageProperty(), image.get(i))));
            }
        }*/
        root.getChildren().add(MascotimageView);

    }

}
