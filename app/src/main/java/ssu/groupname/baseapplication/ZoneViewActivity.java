package ssu.groupname.baseapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.math.MathUtils;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

//import com.jjoe64.graphview.GraphView;

import ssu.groupname.baseapplication.models.Garden;
import ssu.groupname.baseapplication.models.Zone;
import ssu.groupname.baseapplication.network.GardenSearchAsyncTask;

public class ZoneViewActivity extends AppCompatActivity {

    private Garden myGarden;

    //display/select the zone name
    private TextView myZoneNameTextView;

    //display the current temperature values for the zone
    private TextView myLiveTemperatureTextView;
    private ImageView myLiveTemperatureImageView;

    //display the current humidity values for the zone
    private TextView myLiveHumidityTextView;
    private ImageView myLiveHumidityImageView;

    //display the current graph for the zone
    private TextView myGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_view_activity);

        //Get Garden info for the garden we are viewing
        Intent intent = getIntent();
        int gardenId = intent.getIntExtra("GARDEN_ID", 0);
        GardenSearchAsyncTask task = new GardenSearchAsyncTask();
        task.setListener(new GardenSearchAsyncTask.GardenCallbackListener() {
            @Override
            public void onGardenCallback(Garden garden) {
                myGarden = garden;
                viewZone(garden.getZones().get(0));
            }
        });
        task.execute("GET GARDEN INFO BY GARDEN ID (including all zones/sensors/readings)");

        //Link the XML elements
        myZoneNameTextView = findViewById(R.id.zone_selector);
        myLiveTemperatureTextView = findViewById(R.id.live_temperature_text);
        myLiveTemperatureImageView = findViewById(R.id.live_temperature_icon);
        myLiveHumidityTextView = findViewById(R.id.live_humidity_text);
        myLiveHumidityImageView = findViewById(R.id.live_humidity_icon);
        myGraphView = findViewById(R.id.graph);


        //Link the bottom tabs
        //Use TabHost and TabView
    }

    //Update all views with zone information
    public void viewZone(Zone zone) {
        myZoneNameTextView.setText(zone.getName());

        //Compute Average Temperature/Humidity of Sensors in Zone
        double temp = 98.6;
        double humidity = 75;

        myLiveTemperatureTextView.setText(String.format("%f degrees", temp));
        myLiveHumidityTextView.setText(String.format("%f percent", humidity));
        //also update icons

        //Also update GraphView
    }

}
