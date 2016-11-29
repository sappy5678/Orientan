/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ASUS
 */
public class Json_To_Class {

    private static JSONObject Mascot;
    private JSONArray Action_List;

    public Json_To_Class(String Mascot_Type) throws IOException, JSONException {
        //將xml轉成json
        Xml_To_Json xmltojson = new Xml_To_Json(Mascot_Type);
        xmltojson.readjson();
        //將json檔讀出
        Mascot = new JSONObject(xmltojson.readjson());
        //找出需要的資料(Action)
        Action_List = Mascot.getJSONObject("Mascot").getJSONArray("ActionList").getJSONObject(0).getJSONArray("Action");

    }

    public JSONArray getAction_List() {
        return Action_List;
    }

    public int GetListLength() {
        return Action_List.length();
    }

}
