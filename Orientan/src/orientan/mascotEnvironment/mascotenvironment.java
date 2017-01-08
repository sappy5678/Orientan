/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.mascotEnvironment;

import java.io.File;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;

/**
 *
 * @author zp
 */
public final class mascotenvironment {
    //取得螢幕框架

    static private Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    //取得圖片範本
    static Image image;

    static private double LeftWall;
    static private double RightWall;
    static private double Floor;
    static private double ceiling;

    public static void setImage(Image image) {
        mascotenvironment.image = image;
        LeftWall = primScreenBounds.getMinX() - image.getWidth() / 4;
        RightWall = primScreenBounds.getMaxX() - image.getWidth() * 3 / 4;
        Floor = primScreenBounds.getMaxY() - image.getHeight();
        ceiling = primScreenBounds.getMinY();
    }

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
    /*
    //cursor
     public static double detectMouseEnvirment(MouseEvent mouseEvent) {
        return ceiling;
    }*/
}
