/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ASUS
 */
public class PinchedAnimation {

    private String condition_Sign;
    private String condition_Int;
    private Pose pinchedpose = new Pose();

    /*public PinchedAnimation(JSONObject animation_data) throws JSONException {
        //String oricon = animation_data.get("condition").toString();
        /*String[] Cutspa = oricon.split(" ");
        String[] Cutdot = Cutspa[2].split(".");
        String CutdotInt = Cutdot[3].substring(1, Cutdot[3].length() - 1);
        System.out.println(Cutdot[3]);
        //cutspa中的[1]為想要的
        //cutdot[3]為想要的
        String condition = Cutspa[1] + "," + CutdotInt;*/
       // String condition = animation_data.get("condition").toString();

       /* String[] CutCondition;
        CutCondition = condition.split(",");
        String[] Cutspa = CutCondition[0].split(" ");
        // System.out.println(Cutspa[1]);
        // System.out.println(Cutspa[2]);
        String a = Cutspa[2];
        String[] Cutdot = a.split("\\.");

        // System.out.println(CutdotInt);
        //cutspa中的[1]為想要的
        //cutdot[3]為想要的
        condition_Sign = Cutspa[1];
        condition_Int = Cutdot[3].substring(1, Cutdot[3].length() - 2);
        //String finneeded = Cutspa[1] + " " + CutdotInt;*/
        //System.out.println(finneeded);
       /* Pose ppose = new Pose();
        //System.out.println(anaction.getJSONArray("Animation").get(i));     
        ppose.setImage(animation_data.getJSONArray("Animation").getJSONObject(1).getJSONObject("Pose").getString("Image"));
        ppose.setDuration(animation_data.getJSONArray("Animation").getJSONObject(1).getJSONObject("Pose").getInt("Duration"));
        ppose.setImageAnchor(animation_data.getJSONArray("Animation").getJSONObject(1).getJSONObject("Pose").getString("ImageAnchor"));
        ppose.setVelocity(animation_data.getJSONArray("Animation").getJSONObject(1).getJSONObject("Pose").getString("Velocity"));
        this.setPinchedpose(ppose);
        //pinchedpose = new Pose(animation_data.getJSONObject("Pose"));
    }*/

    public PinchedAnimation() {
    }

    public Pose getPinchedpose() {
        return pinchedpose;
    }

    public void setPinchedpose(Pose pinchedpose) {
        this.pinchedpose = pinchedpose;
    }

    public String getCondition_Sign() {
        return condition_Sign;
    }

    public void setCondition_Sign(String condition_Sign) {
        this.condition_Sign = condition_Sign;
    }

    public String getCondition_Int() {
        return condition_Int;
    }

    public void setCondition_Int(String condition_Int) {
        this.condition_Int = condition_Int;
    }



}
