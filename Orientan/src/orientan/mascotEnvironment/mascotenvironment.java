/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.mascotEnvironment;

import java.io.File;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;

/**
 *
 * @author zp
 */
 public final class mascotenvironment {
      //取得螢幕框架
        static private Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        //取得圖片範本
        static Image image = new Image(new File(System.getProperty("user.dir") + "\\img\\shime1.png").toURI().toString());
        static private double LeftWall=primScreenBounds.getMinX();
        static private double RightWall=primScreenBounds.getMaxX()-image.getWidth();
        static private double Floor=primScreenBounds.getMaxY()-image.getHeight();
        static private double ceiling=primScreenBounds.getMinY();
        public static Rectangle2D getPrimScreenBounds() {
        return primScreenBounds;
    }
    public static double getImageWidth() {
        return image.getWidth();
    }
     public static double getImageHeight() {
        return image.getHeight();
    }
    public static double getLeftWall() {
        return LeftWall;
    }

    public static double getRightWall() {
        return RightWall;
    }

    public static double getFloor() {
        return Floor;
    }

    public static double getCeiling() {
        return ceiling;
    }
        
    
}
