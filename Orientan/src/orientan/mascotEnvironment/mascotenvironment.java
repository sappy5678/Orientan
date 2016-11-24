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
        private Rectangle2D primScreenBounds = Screen.getPrimary().getBounds();
        //取得圖片範本
        Image image = new Image(new File(System.getProperty("user.dir") + "\\img\\shime1.png").toURI().toString());
        private double LeftWall=primScreenBounds.getMinX();
        private double RightWall=primScreenBounds.getMaxX()-image.getWidth();
        private double Floor=primScreenBounds.getMaxY()-image.getHeight();
        private double ceiling=primScreenBounds.getMinY();

    public Rectangle2D getPrimScreenBounds() {
        return primScreenBounds;
    }

    public double getLeftWall() {
        return LeftWall;
    }

    public double getRightWall() {
        return RightWall;
    }

    public double getFloor() {
        return Floor;
    }

    public double getCeiling() {
        return ceiling;
    }
        
    
}
