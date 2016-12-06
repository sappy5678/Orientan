/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ASUS
 */
//interface
public class Action {

    /*"Type": "Move",
"Animation": {"Pose": [
{
"ImageAnchor": "64,128",
"Duration": 6,
"Image": "/shime1.png",
"Velocity": "-2,0"
},
{
"ImageAnchor": "64,128",
"Duration": 6,
"Image": "/shime2.png",
"Velocity": "-2,0"
},
{
"ImageAnchor": "64,128",
"Duration": 6,
"Image": "/shime1.png",
"Velocity": "-2,0"
},
{
"ImageAnchor": "64,128",
"Duration": 6,
"Image": "/shime3.png",
"Velocity": "-2,0"
}
]},
"BorderType": "Floor",
"Name": "Walk"
}
     */
    private String Type;
    private ArrayList<Pose> Animation = new ArrayList<Pose>();
    private String BorderType;
    private String Name;

    public Action()
    {
        this.Type="";
        this.BorderType="";
        this.Name="";
    }

    public Action(JSONObject anaction) throws JSONException {
         this.Type = anaction.get("Type").toString();
        Type listType = new TypeToken<ArrayList<Pose>>() {
        }.getType();
        Gson gson = new Gson();
        JSONObject check = anaction.getJSONObject("Animation").optJSONObject("Pose");
        if (check == null) {
            Animation = gson.fromJson(anaction.getJSONObject("Animation").getJSONArray("Pose").toString(), listType);
        } else {
        //直接加入ArrayList
        Pose cha = new Pose();
        cha.setImage(anaction.getJSONObject("Animation").getJSONObject("Pose").getString("Image"));
        cha.setDuration(anaction.getJSONObject("Animation").getJSONObject("Pose").getInt("Duration"));
        cha.setImageAnchor(anaction.getJSONObject("Animation").getJSONObject("Pose").getString("ImageAnchor"));
        cha.setVelocity(anaction.getJSONObject("Animation").getJSONObject("Pose").getString("Velocity"));

        Animation.add(cha);
        
        }
        //System.out.println(anaction.getJSONObject("Animation").getJSONArray("Pose").toString());
        if(anaction.optString("BorderType")!=null)
            this.BorderType = anaction.get("BorderType").toString();
        this.Name = anaction.get("Name").toString();

    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public ArrayList<Pose> getAnimation() {
        return Animation;
    }

    public void setAnimation(ArrayList<Pose> Animation) {
        this.Animation = Animation;
    }

    public String getBorderType() {
        return BorderType;
    }

    public void setBorderType(String BorderType) {
        this.BorderType = BorderType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
}
