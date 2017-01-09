/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ASUS
 */
public class Pose {

    /*{"Pose": {
                    "ImageAnchor": "64,128",
                    "Duration": 250,
                    "Image": "/shime1.png",
                    "Velocity": "0,0"
                }*/
    private String ImageAnchor;
    private int Duration;
    private String Image;
    private String Velocity;

    /*private double VelocityX;
    private double VelocityY;*/
/*
    public Pose(JSONObject pose_data) throws JSONException {
        /*System.out.println(pose_data.get(0).toString());
        this.ImageAnchor = pose_data.get(0).toString();
        this.Duration =Integer.parseInt(pose_data.get(1).toString());
        this.Image = pose_data.get(2).toString();
        this.Velocity = pose_data.get(3).toString();
        //System.out.println("pose結束");
        this.ImageAnchor = pose_data.get("ImageAnchor").toString();
        this.Duration = Integer.parseInt(pose_data.get("Duration").toString());
        this.Image = pose_data.get("Image").toString();
        this.Velocity = pose_data.get("Velocity").toString();

    }
*/
    Pose() {

    }

    public double getVelocityX() {
        String[] token = this.Velocity.split(",|\n");
        return Double.parseDouble(token[0]);
    }

    public double getVelocityY() {
        String[] token = this.Velocity.split(",|\n");
        return Double.parseDouble(token[1]);
    }

    public String getImageAnchor() {
        return ImageAnchor;
    }

    public void setImageAnchor(String ImageAnchor) {
        this.ImageAnchor = ImageAnchor;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int Duration) {
        this.Duration = Duration;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getVelocity() {
        return Velocity;
    }

    public void setVelocity(String Velocity) {
        this.Velocity = Velocity;
    }
}
