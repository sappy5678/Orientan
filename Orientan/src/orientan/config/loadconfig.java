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

    public loadconfig(String target) throws IOException, JSONException {
        Json_To_Class actiondata = new Json_To_Class(target);
        int listlength = actiondata.GetListLength();

        JSONArray List = actiondata.getAction_List();
        /*for (int i = 0; i < listlength; i++) {

        }*/

        // Action_List = new ArrayList<Action>();
        for (int i = 3; i < 6; i++) {
            Action_List.add(new Action(actiondata.getAction_List().getJSONObject(i)));
            //System.out.println(i);
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
}
