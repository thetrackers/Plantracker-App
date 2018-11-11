package ssu.groupname.baseapplication.network;

import android.os.AsyncTask;
import java.io.IOException;
import java.util.List;
import okhttp3.Response;
import ssu.groupname.baseapplication.models.Garden;
import ssu.groupname.baseapplication.models.User;
import ssu.groupname.baseapplication.utils.GardenParser;
import ssu.groupname.baseapplication.utils.UserParser;

public class GardenSearchAsyncTask extends AsyncTask<String, Void, Garden> {

    //private final String baseUrl = "SERVERURL"; //put server URL in resources later;

    private GardenCallbackListener myListener;

    //sample hardcoded JSON string for testing without server connection
    private final String testResponse = "{\n" +
            "      \"myGardenName\": \"My Garden\",\n" +
            "      \"myGardenId\": \"2\",\n" +
            "      \"myZones\": [\n" +
            "        {\n" +
            "          \"myZoneName\": \"My Tomatoes\",\n" +
            "          \"myZoneId\": \"3\",\n" +
            "          \"myHighTemperatureThreshold\": \"100\",\n" +
            "          \"myLowTemperatureThreshold\": \"50\",\n" +
            "          \"myHighHumidityThreshold\": \"80\",\n" +
            "          \"myLowHumidityThreshold\": \"40\",\n" +
            "          \"mySensors\": [\n" +
            "            {\n" +
            "              \"mySensorName\": \"Tomato 1\",\n" +
            "              \"mySensorId\": \"4\",\n" +
            "              \"myReadings\": [\n" +
            "                {\n" +
            "                  \"mySensorId\": \"4\",\n" +
            "                  \"myTimestamp\": \"1541902831655\",\n" +
            "                  \"myTemperature\": \"75\",\n" +
            "                  \"myHumidity\": \"70\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"mySensorId\": \"5\",\n" +
            "                  \"myTimestamp\": \"1541902931655\",\n" +
            "                  \"myTemperature\": \"80\",\n" +
            "                  \"myHumidity\": \"65\"\n" +
            "                }\n" +
            "              ]\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        {\n" +
            "          \"myZoneName\": \"My Potatoes\",\n" +
            "          \"myZoneId\": \"6\",\n" +
            "          \"myHighTemperatureThreshold\": \"125\",\n" +
            "          \"myLowTemperatureThreshold\": \"10\",\n" +
            "          \"myHighHumidityThreshold\": \"70\",\n" +
            "          \"myLowHumidityThreshold\": \"20\",\n" +
            "          \"mySensors\": [\n" +
            "            {\n" +
            "              \"mySensorName\": \"Potato 1\",\n" +
            "              \"mySensorId\": \"7\",\n" +
            "              \"myReadings\": [\n" +
            "                {\n" +
            "                  \"mySensorId\": \"7\",\n" +
            "                  \"myTimestamp\": \"1541902831655\",\n" +
            "                  \"myTemperature\": \"70\",\n" +
            "                  \"myHumidity\": \"60\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"mySensorId\": \"7\",\n" +
            "                  \"myTimestamp\": \"1541902931655\",\n" +
            "                  \"myTemperature\": \"60\",\n" +
            "                  \"myHumidity\": \"80\"\n" +
            "                }\n" +
            "              ]\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "}";

    @Override
    protected Garden doInBackground(String... params) {
        String gardenId = params[0]; //garden id

        //OkHttpClient client = new OkHttpClient();
        //HttpUrl.parse(baseUrl).newBuilder()
        //        .addQueryParameter("_user_id", userParam)
        //        .build();

        //Request request = new Request.Builder()
        //        .url(url)
        //        .build();

        /* try {
            Response response = client.newCall(request).execute();
            if (response != null) {
                return UserParser.userFromJson(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } */

        return GardenParser.gardenFromJson(testResponse);
    }

    @Override
    protected void onPostExecute(Garden garden) {myListener.onGardenCallback(garden);}

    public void setListener(GardenCallbackListener listener) {this.myListener = listener;}

    public interface GardenCallbackListener {
        void onGardenCallback(Garden garden);
    }
}
