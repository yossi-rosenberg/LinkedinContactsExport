import dataTypes.UserDetails;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonUtils {
    public static JSONObject parseContactsJson(List<WebElement> contactsName,  List<WebElement> contactsPOW, UserDetails userNamePOWAndLocation) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("myName",userNamePOWAndLocation.getName());
        jsonObject.put("myWorkplace",userNamePOWAndLocation.getPlaceOfWork());
        jsonObject.put("city",userNamePOWAndLocation.getLocation());
        JSONArray connectionsArray = new JSONArray();
        for (int i = 0; i < contactsName.size(); i++) {
                connectionsArray.put(contactsName.get(i).getText().toString().split("\n")[0]);
            connectionsArray.put(contactsPOW.get(i).getText().toString());
        }
        jsonObject.put( "connections",connectionsArray);
        return jsonObject;
    }

    public static void writeJsonObjectToFile(JSONObject jsonObject){
        try {
            FileWriter file = new FileWriter("connections.json");
            file.write(jsonObject.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}