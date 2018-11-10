package ssu.groupname.baseapplication;

import android.os.Bundle;
import android.support.v4.math.MathUtils;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

//import com.jjoe64.graphview.GraphView;

import ssu.groupname.baseapplication.models.Zone;

public class ZoneViewActivity extends AppCompatActivity {



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

        //Link the XML for the zone view activity
        setContentView(R.layout.zone_view_activity);

        //Link the elements
        //Link where we display the zone name + also maybe select the zone
        myZoneNameTextView = findViewById(R.id.zone_selector);

        //Link where we display the current/most recent zone temperature
        myLiveTemperatureTextView = findViewById(R.id.live_temperature_text);
        myLiveTemperatureImageView = findViewById(R.id.live_temperature_icon);

        //Link where we display the current/most recent zone humidity
        myLiveHumidityTextView = findViewById(R.id.live_humidity_text);
        myLiveHumidityImageView = findViewById(R.id.live_humidity_icon);

        //Link the 24hr readings to the graph
        myGraphView = findViewById(R.id.graph);

        //Link the bottom tabs
        //Use TabHost and TabView
    }

    //Update all views with zone information
    public void viewZone(Zone zone) {
        myZoneNameTextView.setText(zone.getName());

        //Compute Average Temperature/Humidity of Sensors in Zone
        double temp = 98;
        double humidity = 75;

        myLiveTemperatureTextView.setText(String.format("%d degrees", temp));
        myLiveHumidityTextView.setText(String.format("%d %", humidity));
        //also update icons

        //Also update GraphView
    }

}
