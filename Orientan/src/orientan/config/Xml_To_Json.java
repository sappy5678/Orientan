/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientan.config;

/**
 *
 * @author ASUS
 */
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class Xml_To_Json {
//C:\Users\ASUS\Desktop\xmltojson\shimeji-ee-master\conf

    private static int PRETTY_PRINT_INDENT_FACTOR = 0;
    private static String conf_type;
    private static String jsonPrettyPrintString;
    private static JSONObject xmlJSONObj;

    public Xml_To_Json(String ConfType) throws FileNotFoundException, IOException {

        try {
            //讀檔
            conf_type = ConfType;
            String xmlpath = System.getProperty("user.dir") + "\\conf\\" + conf_type + ".xml";
            File xmlfile = new File(xmlpath);
            FileReader fr = new FileReader(xmlfile);
            BufferedReader br = new BufferedReader(fr);//用正常IO來讀檔 
            String TEST_XML_STRING = null;
            int temp = 0;
            StringBuffer temp2 = new StringBuffer();
            while ((temp = br.read()) != -1) {

                if (temp != 10 && temp != 13) {
                    temp2.append((char) temp);
                    TEST_XML_STRING = temp2.toString();
                }

            }
            //System.out.println(TEST_XML_STRING);
            //轉成json
            xmlJSONObj = XML.toJSONObject(TEST_XML_STRING);
            jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
            //刪除特殊字元
            //System.out.print(jsonPrettyPrintString);
            //jsonPrettyPrintString = jsonPrettyPrintString.replace("\n", "");
            //System.out.print(jsonPrettyPrintString);
            //存入資料夾
            String jsonpath = System.getProperty("user.dir") + "\\conf\\" + conf_type + ".json";
            FileWriter fw = new FileWriter(jsonpath);
            fw.write(jsonPrettyPrintString);
            fw.close();
            /* //刪除\n
           //因為讀取後，自動將\n視為字串，而不是特殊字元
           //因此要刪除\\n
            json_data=json_data.replace("\\n", "");*/
        } catch (JSONException je) {
            System.out.print(je.toString());
        }

    }

    public String readjson() throws FileNotFoundException, IOException {
        //讀json檔
        String jsonpath = System.getProperty("user.dir") + "\\conf\\" + conf_type + ".json";
        File readjson = new File(jsonpath);

        FileReader fr_json = new FileReader(readjson);

        BufferedReader br_json = new BufferedReader(fr_json);
        String json_data = null;
        int json_temp = 0;
        StringBuffer json_sb = new StringBuffer();
        while ((json_temp = br_json.read()) != -1) {

            if (json_temp == 10) {

            } else {
                json_sb.append((char) json_temp);
                json_data = json_sb.toString();

            }

        }
        return json_data;
    }
}
