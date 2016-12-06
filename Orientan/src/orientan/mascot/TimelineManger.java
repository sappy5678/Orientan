/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.mascot;

import java.util.ArrayList;
import javafx.animation.Timeline;

/**
 *
 * @author zp
 */
public class TimelineManger {
    private ArrayList<Timeline> timelineList=new ArrayList<Timeline>();

    public ArrayList<Timeline> getTimelineList() {
        return timelineList;
    }
    public void StopAll()
    {
        for(int i=0;i<timelineList.size();i++)
            timelineList.get(i).stop();
    }
}
