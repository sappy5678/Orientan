/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.config;

import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ASUS
 */
public class loadconfig {

    private static ArrayList<Action> Action_List = new ArrayList<Action>();
    private static FallingData falling_data = new FallingData();
    private static PinchedData pinched_data;
    private static ResistingData resisting_data;

    public loadconfig(String target) throws IOException, JSONException {
        Json_To_Class actiondata = new Json_To_Class(target);
        int listlength = actiondata.GetListLength();

        JSONArray List = actiondata.getAction_List();
        
        /*for (int i = 0; i < listlength; i++) {

        }*/

        // Action_List = new ArrayList<Action>();
        for (int i = 2, j = 0; i < 29; i++) {
            //climb的格式與pinched類似
            if (i == 8 || i == 18 || i == 19) {
                System.out.println(i);
                System.out.println(actiondata.getAction_List().getJSONObject(i).get("Name"));
                continue;
            } else if (i == 24) {
                falling_data.setType(actiondata.getAction_List().getJSONObject(i).get("Type").toString());
                falling_data.setRegistanceX(actiondata.getAction_List().getJSONObject(i).getDouble("RegistanceX"));
                falling_data.setRegistanceY(actiondata.getAction_List().getJSONObject(i).getDouble("RegistanceY"));
                falling_data.setName(actiondata.getAction_List().getJSONObject(i).get("Name").toString());
                falling_data.setGravity(actiondata.getAction_List().getJSONObject(i).getDouble("Gravity"));
                falling_data.setFallingAction(new Action(actiondata.getAction_List().getJSONObject(24)));
            } else if (i == 27) {
                pinched_data = new PinchedData(actiondata.getAction_List().getJSONObject(i));
                /*System.out.println(pinched_data.getName());
                System.out.println(pinched_data.getType());
                System.out.println(pinched_data.getAnimation().get(0).getCondition_Int());
                System.out.println(pinched_data.getAnimation().get(0).getCondition_Sign());
                System.out.println(pinched_data.getAnimation().get(0).getPinchedpose().getImage());*/

            } else if (i == 28) {
                resisting_data = new ResistingData(actiondata.getAction_List().getJSONObject(i));
                //System.out.println(resisting_data.getAnimation());
            } else {
                Action_List.add(new Action(actiondata.getAction_List().getJSONObject(i)));
                /*Action a = Action_List.get(j);
                if(j==18)
                {System.out.println(a.getType());
                System.out.println(a.getName());
                System.out.println(a.getVelocityParam());
                System.out.println(a.getAnimation());
                System.out.println(j);}*/
                j++;
            }
        }
        //Action_List.add(new Action(actiondata.getAction_List().getJSONObject(3)));//move floor walk
        //Action a = Action_List.get(0);
        //System.out.println(a.getBorderType());
        //System.out.println(a.getType());
        //System.out.println(a.getName());
        //System.out.println(a.getAnimation().size());
    }

    public Action getData(String name, String type) {

        Action returnAction = new Action();//初始化    
        for (int i = 0; i < Action_List.size(); i++) {
            if (Action_List.get(i).getName().endsWith(name) && Action_List.get(i).getType().endsWith(type)) {
                returnAction = Action_List.get(i);

                break;
            }
        }
        return returnAction;
    }

    public FallingData getFallingData() {
        return falling_data;
    }

    public static PinchedData getPinched_data() {
        return pinched_data;
    }

    public static ResistingData getResisting_data() {
        return resisting_data;
    }

}
