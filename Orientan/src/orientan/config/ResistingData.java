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
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ASUS
 */
public class ResistingData {
    
    private String Type;
    private ArrayList<Pose> Animation = new ArrayList<Pose>();
    private String Name;
    public ResistingData(JSONObject anaction) throws JSONException {
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
        this.Name = anaction.get("Name").toString();

    }

    public ResistingData() {
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

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
    

}
