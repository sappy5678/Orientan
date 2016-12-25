/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.mascotEnvironment;

import javafx.scene.input.MouseEvent;

/**
 *
 * @author zp
 */
public class Mouse {

    private long lastTime = System.currentTimeMillis();
    private double newX;
    private double newY;
    private double oldX;
    private double oldY;
    private double speedPowTwo;
    private boolean speedXIsPositive;
    private double mouseSpeedX;
    private boolean speedYIsPositive;
    private double mouseSpeedY;

    public boolean isSpeedXIsPositive() {
        return speedXIsPositive;
    }

    public double getMouseSpeedX() {
        return mouseSpeedX;
    }

    public boolean isSpeedYIsPositive() {
        return speedYIsPositive;
    }

    public double getMouseSpeedY() {
        return mouseSpeedY;
    }

    public double getNewX() {
        return newX;
    }

    public double getNewY() {
        return newY;
    }

    public void updateMouseData(MouseEvent mouseEvent) {
        if (System.currentTimeMillis() - lastTime >= 10) {
            lastTime = System.currentTimeMillis();
            oldX = newX;
            oldY = newY;
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
                lastTime = System.currentTimeMillis();
            }
        }
    }
}
