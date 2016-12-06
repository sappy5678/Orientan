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
public class PinchedData {

    private String Type;
    private ArrayList<PinchedAnimation> Animation = new ArrayList<PinchedAnimation>();
    private String Name;
    

    public PinchedData(JSONObject anaction) throws JSONException {
        this.Type = anaction.get("Type").toString();

        //PinchedAnimation cha = new PinchedAnimation();
        /* Type listType = new TypeToken<ArrayList<PinchedAnimation>>() {
        }.getType();
        Gson gson = new Gson();*/
        //System.out.println(anaction.getJSONArray("Animation"));
        /*Animation = gson.fromJson(anaction.getJSONArray("Animation").toString(), listType);
        System.out.println(Animation.get(0).getCondition());*/
        for (int i = 0; i < anaction.getJSONArray("Animation").length(); i++) { //將condition切開
            String[] CutCondition;
            CutCondition = anaction.getJSONArray("Animation").get(i).toString().split(",");
            String[] Cutspa = CutCondition[0].split(" ");
            // System.out.println(Cutspa[1]);
            // System.out.println(Cutspa[2]);
            String a = Cutspa[2];
            String[] Cutdot = a.split("\\.");

            String CutdotInt = Cutdot[3].substring(1, Cutdot[3].length() - 2);
            // System.out.println(CutdotInt);
            //cutspa中的[1]為想要的
            //cutdot[3]為想要的
            //String finneeded = Cutspa[1] + " " + CutdotInt;
            //System.out.println(finneeded);
            PinchedAnimation cha = new PinchedAnimation();
            Pose ppose = new Pose();
            //System.out.println(anaction.getJSONArray("Animation").get(i));     
            ppose.setImage(anaction.getJSONArray("Animation").getJSONObject(1).getJSONObject("Pose").getString("Image"));
            ppose.setDuration(anaction.getJSONArray("Animation").getJSONObject(1).getJSONObject("Pose").getInt("Duration"));
            ppose.setImageAnchor(anaction.getJSONArray("Animation").getJSONObject(1).getJSONObject("Pose").getString("ImageAnchor"));
            ppose.setVelocity(anaction.getJSONArray("Animation").getJSONObject(1).getJSONObject("Pose").getString("Velocity"));
            
            cha.setCondition_Sign(Cutspa[1]);
            cha.setCondition_Int(CutdotInt);
           // cha.setCondition(finneeded);
            cha.setPinchedpose(ppose);
            /*anaction.getJSONObject("Animation").getJSONObject("Pose").getString("Image"));
            cha.setDuration(anaction.getJSONObject("Animation").getJSONObject("Pose").getInt("Duration"));
            cha.setImageAnchor(anaction.getJSONObject("Animation").getJSONObject("Pose").getString("ImageAnchor"));
            cha.setVelocity(anaction.getJSONObject("Animation").getJSONObject("Pose").getString("Velocity"));*/
            //System.out.println(cha);
            Animation.add(cha);

            //System.out.println(anaction.getJSONObject("Animation").getJSONArray("Pose").toString());
            this.Name = anaction.get("Name").toString();
        }

    }

    public String getType() {
        return Type;
    }

    public ArrayList<PinchedAnimation> getAnimation() {
        return Animation;
    }

    public String getName() {
        return Name;
    }

}
