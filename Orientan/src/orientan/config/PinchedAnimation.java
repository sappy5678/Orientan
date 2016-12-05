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

    private String condition;
    private Pose pinchedpose=new Pose();

    public PinchedAnimation(JSONObject animation_data) throws JSONException {
        //String oricon = animation_data.get("condition").toString();
        /*String[] Cutspa = oricon.split(" ");
        String[] Cutdot = Cutspa[2].split(".");
        String CutdotInt = Cutdot[3].substring(1, Cutdot[3].length() - 1);
        System.out.println(Cutdot[3]);
        //cutspa中的[1]為想要的
        //cutdot[3]為想要的
        String condition = Cutspa[1] + "," + CutdotInt;*/
        condition = animation_data.get("condition").toString();
        pinchedpose=new Pose(animation_data.getJSONObject("Pose"));
    }

    public PinchedAnimation() {
    }

    public Pose getPinchedpose() {
        return pinchedpose;
    }

    public void setPinchedpose(Pose pinchedpose) {
        this.pinchedpose = pinchedpose;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

}
