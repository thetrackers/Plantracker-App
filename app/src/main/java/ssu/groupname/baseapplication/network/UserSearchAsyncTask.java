package ssu.groupname.baseapplication.network;

import android.os.AsyncTask;
import java.io.IOException;
import java.util.List;
import okhttp3.Response;
import ssu.groupname.baseapplication.models.User;
import ssu.groupname.baseapplication.utils.UserParser;

public class UserSearchAsyncTask extends AsyncTask<String, Void, User> {

    //private final String baseUrl = "SERVERURL"; //put server URL in resources later;

    private UserCallbackListener myListener;

    //sample hardcoded JSON string for testing without server connection
    private final String testResponse = "{\n" +
            "  \"myFirstName\": \"Billy\",\n" +
            "  \"myLastName\": \"Bob\",\n" +
            "  \"myUserId\": \"1\",\n" +
            "  \"myTelephoneNumber\": \"7077011107\",\n" +
            "  \"myGardens\": [\n" +
            "    {\n" +
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
            "                }\n" +
            "              ]\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Override
    protected User doInBackground(String... params) {
        String userId = params[0]; //user id

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

        return UserParser.userFromJson(testResponse);
    }

    @Override
    protected void onPostExecute(User user) {myListener.onUserCallback(user);}

    public void setListener(UserCallbackListener listener) {this.myListener = listener;}

    public interface UserCallbackListener {
        void onUserCallback(User user);
    }
}
