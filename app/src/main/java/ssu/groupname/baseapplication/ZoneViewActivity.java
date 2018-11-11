package ssu.groupname.baseapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.Date;
import java.util.Iterator;

import ssu.groupname.baseapplication.models.Garden;
import ssu.groupname.baseapplication.models.Reading;
import ssu.groupname.baseapplication.models.Sensor;
import ssu.groupname.baseapplication.models.Zone;
import ssu.groupname.baseapplication.network.GardenSearchAsyncTask;

public class ZoneViewActivity extends AppCompatActivity {

    private Garden myGarden;

    private TabHost myTabHost;

    //strip to display zone name and switch zones
    private Button myPreviousZoneButton;
    private TextView myZoneNameTextView;
    private Button myNextZoneButton;
    private int myZoneIndex = 0; //by default we view the first zone in the garden
    private int myNumZones = 0;  //we need these variables to iterate through zone list

    //TAB 1
    //display the current temperature/humidity values for the zone
    private TextView myLiveTemperatureTextView;
    private ImageView myLiveTemperatureImageView;
    private TextView myLiveHumidityTextView;
    private ImageView myLiveHumidityImageView;

    //display the graph for the zone
    private GraphView myGraphView;

    //TAB2
    //Display thresholds
    private TextView myHighTemperatureThreshold;
    private TextView myLowTemperatureThreshold;
    private TextView myHighHumidityThreshold;
    private TextView myLowHumidityThreshold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_view_activity);

        //Set up the tab system
        myTabHost = findViewById(R.id.tab_host);
        myTabHost.setup();
        //Tab 1
        TabHost.TabSpec spec = myTabHost.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("View Data"); //we can put an icon here
        myTabHost.addTab(spec);
        //Tab 2
        spec = myTabHost.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Set Thresholds");
        myTabHost.addTab(spec);

        //Get Garden info for the garden we are viewing
        Intent intent = getIntent();
        int gardenId = intent.getIntExtra("GARDEN_ID", 0);
        GardenSearchAsyncTask task = new GardenSearchAsyncTask();
        task.setListener(new GardenSearchAsyncTask.GardenCallbackListener() {
            @Override
            public void onGardenCallback(Garden garden) {
                myGarden = garden;

                //Set up zone selector strip
                myPreviousZoneButton = findViewById(R.id.previous_zone_button);
                myZoneNameTextView = findViewById(R.id.zone_selector);
                myNextZoneButton = findViewById(R.id.next_zone_button);
                myNumZones = myGarden.getZones().size();
                myPreviousZoneButton.setText("X");
                myPreviousZoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myZoneIndex > 0) {
                            myZoneIndex--;
                            myNextZoneButton.setText(">");
                            if (myZoneIndex == 0) {
                                myPreviousZoneButton.setText("X");
                            }
                            viewZone(myGarden.getZones().get(myZoneIndex));
                        }
                    }
                });
                if (myNumZones <= 1) { myNextZoneButton.setText("X"); }
                else { myNextZoneButton.setText(">"); }
                myNextZoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myZoneIndex < myNumZones-1) {
                            myZoneIndex++;
                            myPreviousZoneButton.setText("<");
                            if (myZoneIndex == myNumZones-1) {
                                myNextZoneButton.setText("X");
                            }
                            viewZone(myGarden.getZones().get(myZoneIndex));
                        }
                    }
                });

                //Populate view with information
                viewZone(garden.getZones().get(0));
            }
        });
        task.execute("GET GARDEN INFO BY GARDEN ID (including all zones/sensors/readings)");

        //Link the other xml elements for display zone information
        //TAB 1
        myLiveTemperatureTextView = findViewById(R.id.live_temperature_text);
        myLiveTemperatureImageView = findViewById(R.id.live_temperature_icon);
        myLiveHumidityTextView = findViewById(R.id.live_humidity_text);
        myLiveHumidityImageView = findViewById(R.id.live_humidity_icon);
        myGraphView = findViewById(R.id.graph);
        //TAB 2
        myHighTemperatureThreshold = findViewById(R.id.high_temperature_threshold);
        myLowTemperatureThreshold = findViewById(R.id.low_temperature_threshold);
        myHighHumidityThreshold = findViewById(R.id.high_humidity_threshold);
        myLowHumidityThreshold = findViewById(R.id.low_humidity_threshold);

    }

    //Update all views with zone information
    public void viewZone(Zone zone) {
        myZoneNameTextView.setText(zone.getName());

        //TAB 1
        //Draw graph  -- currently drawing EVERY sensor on the graph, not just average
        //Must iterate through sensors and readings to create data points
        int maxReadings = 0; //number of readings from the sensor with the most readings
        int numberOfSensors = zone.getSensors().size();
        double latestTemperature = 0;  //Will represent average latest temperature
        double latestHumidity = 0;  //Will represent average latest humidity
        for (Sensor sensor : zone.getSensors()) {
            int numberOfReadings = sensor.getReadings().size();
            DataPoint[] temperatureArray = new DataPoint[numberOfReadings];
            DataPoint[] humidityArray = new DataPoint[numberOfReadings];

            Reading reading = sensor.getReadings().get(0); //This will be the latest reading from this sensor when we exit loop
            for (int i = 0; i < numberOfReadings; i++) {
                reading = sensor.getReadings().get(i);
                Date date = new Date(reading.getTimestamp());  //converts long to date
                temperatureArray[i] = new DataPoint(date, reading.getTemperature());
                humidityArray[i] = new DataPoint(date, reading.getHumidity());
            }

            latestTemperature += reading.getTemperature();
            latestHumidity += reading.getHumidity();

            LineGraphSeries<DataPoint> temperatureSeries = new LineGraphSeries<>(temperatureArray);
            temperatureSeries.setTitle(sensor.getName() + " temperature");
            temperatureSeries.setColor(Color.RED);
            temperatureSeries.setDrawDataPoints(true);

            LineGraphSeries<DataPoint> humiditySeries = new LineGraphSeries<>(humidityArray);
            humiditySeries.setTitle(sensor.getName() + " humidity");
            humiditySeries.setColor(Color.BLUE);
            humiditySeries.setDrawDataPoints(true);

            myGraphView.removeAllSeries(); //reset the graph
            myGraphView.addSeries(temperatureSeries);
            myGraphView.addSeries(humiditySeries);
        }

        //Get average latest temperature/humidity
        latestTemperature /= numberOfSensors;
        latestHumidity /= numberOfSensors;

        // set date label formatter
        myGraphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(ZoneViewActivity.this));
        //myGraphView.getGridLabelRenderer().setNumHorizontalLabels(3);
        //myGraphView.getGridLabelRenderer().setHumanRounding(false);

            /* //Can append datapoints to series instead
            LineGraphSeries<DataPoint> temperatureSeries = new LineGraphSeries<>(new DataPoint[] {});
            LineGraphSeries<DataPoint> humiditySeries = new LineGraphSeries<>(new DataPoint[] {});
            for (Reading reading : sensor.getReadings()) {
                Date date = new Date(reading.getTimestamp());
                DataPoint temperaturePoint = new DataPoint(date, reading.getTemperature());
                temperatureSeries.appendData(temperaturePoint, )
            }
            */
            //double currentTime = System.currentTimeMillis();

        //display latest readings
        myLiveTemperatureTextView.setText(String.format("%.1f degrees", latestTemperature));
        myLiveHumidityTextView.setText(String.format("%.1f percent", latestHumidity));


        //TAB 2
        myHighTemperatureThreshold.setText(String.format("%d degrees",zone.getHighTemperatureThreshold()));
        myLowTemperatureThreshold.setText(String.format("%d degrees",zone.getLowTemperatureThreshold()));
        myHighHumidityThreshold.setText(String.format("%d percent", zone.getHighHumidityThreshold()));
        myLowHumidityThreshold.setText(String.format("%d percent", zone.getLowHumidityThreshold()));
    }

}
