/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.mascot;

import java.io.File;
import java.util.ArrayList;
import javafx.animation.Timeline;
import javafx.scene.image.Image;

/**
 *
 * @author zp
 */
public class TimelineManger {
    private String imgPath="";
    private Image standDefaultImage;
    private Image sitDefaultImage;
    private ArrayList<Timeline> timelineList = new ArrayList<Timeline>();
    public TimelineManger(String InimgPath)
    {
        this.imgPath=InimgPath;
        standDefaultImage=new Image(new File(imgPath + "\\shime1.png").toURI().toString());
        sitDefaultImage=new Image(new File(imgPath + "\\shime11.png").toURI().toString());
    }
    public ArrayList<Timeline> getTimelineList() {
        return timelineList;
    }

    public void StopAll() {
        for (int i = 0; i < timelineList.size(); i++) {
            timelineList.get(i).stop();
        }
    }
}
