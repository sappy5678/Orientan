/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.mascot;

import java.io.File;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author zp
 */
public class mascot {
    double deltaX=-20;
    public mascot() {

        javafx.scene.control.Button btn = new javafx.scene.control.Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

            @Override
            public void handle(javafx.event.ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        /**/
        //設定透明顯示視窗
        //stage透明化
        Stage mascotStage = new Stage();       
        mascotStage.setAlwaysOnTop(true);   //讓其永遠在最上層
        mascotStage.initStyle(StageStyle.TRANSPARENT);
        AnchorPane root = new AnchorPane();
        //設定root顏色
        //root.getChildren().add(btn);
        root.setStyle("-fx-background:transparent;");
        //設定scene顏色與大小
        Scene scene = new Scene(root);
        scene.setFill(null);
        
        mascotStage.setScene(scene);
        mascotStage.show();
        //配置動畫圖片  
        Image image1 = new Image(new File(System.getProperty("user.dir") + "\\img\\shime1.png").toURI().toString());
        Image image2 = new Image(new File(System.getProperty("user.dir") + "\\img\\shime2.png").toURI().toString());
        Image image3 = new Image(new File(System.getProperty("user.dir") + "\\img\\shime3.png").toURI().toString());
        ImageView imageView = new ImageView();
         //取得螢幕框架
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        //設定視窗初始位置
        mascotStage.setY(primScreenBounds.getMaxY()-140);
        mascotStage.setX(primScreenBounds.getMaxX()-100);
        //事件監聽  
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //如果超出左右邊界，就變換前進方向與圖片翻轉
                if(mascotStage.getX()<=-20||mascotStage.getX()>=primScreenBounds.getWidth())
                {
                    deltaX=deltaX * (-1);
                    imageView.setRotationAxis(Rotate.Y_AXIS);
                    if(deltaX>0)
                        imageView.setRotate(180);
                    else
                        imageView.setRotate(0);
                }
                mascotStage.setX((mascotStage.getX() +deltaX));
            }
        };
        //設定時間軸
        KeyFrame start;
        KeyFrame end;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(imageView.imageProperty(), image2)),
                //new KeyFrame(Duration.seconds(0.5), new KeyValue(imageView.imageProperty(), image1)),
                new KeyFrame(Duration.seconds(0.5), onFinished, new KeyValue(imageView.imageProperty(), image1)),
                new KeyFrame(Duration.seconds(1), new KeyValue(imageView.imageProperty(), image3)),
                new KeyFrame(Duration.seconds(1.5), onFinished, new KeyValue(imageView.imageProperty(), image1)),
                //下面這行是給不斷循環的時間軸在下一次循環的緩衝，如果刪掉的話，這個最後一個影格會因為太快而看不到
                new KeyFrame(Duration.seconds(2), new KeyValue(imageView.imageProperty(), image1))
        );
       
        
        root.getChildren().add(imageView);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

}
