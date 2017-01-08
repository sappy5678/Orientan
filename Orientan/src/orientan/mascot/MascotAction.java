/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.mascot;

/**
 *
 * @author zp
 */
public abstract class MascotAction {

    String imagePath;

    public abstract void play();

    public abstract void play(int circleTime);
}
