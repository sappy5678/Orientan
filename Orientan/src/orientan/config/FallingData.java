/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.config;

/**
 *
 * @author ASUS
 */
public class FallingData {

    private double RegistanceX;
    private double RegistanceY;
    private double Gravity;
    private String Name;
     private String Type;

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
    public double getRegistanceX() {
        return RegistanceX;
    }

    public void setRegistanceX(double RegistanceX) {
        this.RegistanceX = RegistanceX;
    }

    public double getRegistanceY() {
        return RegistanceY;
    }

    public void setRegistanceY(double RegistanceY) {
        this.RegistanceY = RegistanceY;
    }

    public double getGravity() {
        return Gravity;
    }

    public void setGravity(double Gravity) {
        this.Gravity = Gravity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

}
