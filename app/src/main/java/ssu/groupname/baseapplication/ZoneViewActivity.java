package ssu.groupname.baseapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
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
    final int MAX_TEMPERATURE_THRESHOLD = 150;
    final int MIN_TEMPERATURE_THRESHOLD = -50;
    final int MAX_HUMIDITY_THRESHOLD = 100;
    final int MIN_HUMIDITY_THRESHOLD = 0;

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
    //Display/set thresholds
    private Spinner myHighTemperatureThreshold;
    private Spinner myLowTemperatureThreshold;
    private Spinner myHighHumidityThreshold;
    private Spinner myLowHumidityThreshold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_view_activity);

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

        //Set up the spinners for threshold selection
        //Create the list of possible choices for temperature thresholds
        Integer[] tempChoices = new Integer[MAX_TEMPERATURE_THRESHOLD - MIN_TEMPERATURE_THRESHOLD + 1];
        Integer value = MIN_TEMPERATURE_THRESHOLD;
        for (int i = 0; i < tempChoices.length; i++) {
            tempChoices[i] = value;
            value++;
        }
        //Create adapter for high temperature
        ArrayAdapter<Integer> highTemperatureThresholdAdapter = new ArrayAdapter<Integer>(
                ZoneViewActivity.this,
                R.layout.spinner_item,
                tempChoices
        );
        highTemperatureThresholdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myHighTemperatureThreshold.setAdapter(highTemperatureThresholdAdapter);
        //Create adapter for low temperature
        ArrayAdapter<Integer> lowTemperatureThresholdAdapter = new ArrayAdapter<Integer>(
                ZoneViewActivity.this,
                R.layout.spinner_item,
                tempChoices
        );
        lowTemperatureThresholdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myLowTemperatureThreshold.setAdapter(lowTemperatureThresholdAdapter);
        //Create the list of possible choices for humidity thresholds
        Integer[] humChoices = new Integer[MAX_HUMIDITY_THRESHOLD - MIN_HUMIDITY_THRESHOLD + 1];
        value = MIN_HUMIDITY_THRESHOLD;
        for (int i = 0; i < humChoices.length; i++) {
            humChoices[i] = value;
            value++;
        }
        //Create adapter for high humidity
        ArrayAdapter<Integer>highHumidityThresholdAdapter = new ArrayAdapter<Integer>(
                ZoneViewActivity.this,
                R.layout.spinner_item,
                humChoices
        );
        highHumidityThresholdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myHighHumidityThreshold.setAdapter(highHumidityThresholdAdapter);
        //Create adapter for low humidity
        ArrayAdapter<Integer> lowHumidityThresholdAdapter = new ArrayAdapter<Integer>(
                ZoneViewActivity.this,
                R.layout.spinner_item,
                humChoices
        );
        lowHumidityThresholdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myLowHumidityThreshold.setAdapter(lowHumidityThresholdAdapter);

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
        myGraphView.removeAllSeries(); //reset the graph
        myGraphView.getSecondScale().removeAllSeries();
        long latestReadingTime = 0;
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

            if (reading.getTimestamp() > latestReadingTime) {
                latestReadingTime = reading.getTimestamp();
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

            myGraphView.addSeries(temperatureSeries);
            //myGraphView.addSeries(humiditySeries);
            myGraphView.getSecondScale().addSeries(humiditySeries);


        }

        //Get average latest temperature/humidity
        latestTemperature /= numberOfSensors;
        latestHumidity /= numberOfSensors;
        //display latest readings
        myLiveTemperatureTextView.setText(String.format("%.1f degrees", latestTemperature));
        myLiveHumidityTextView.setText(String.format("%.1f percent", latestHumidity));
        //If average latest reading is outside of threshold change the icons
        if (latestTemperature > zone.getHighTemperatureThreshold()) {
            myLiveTemperatureImageView.setImageResource(R.drawable.hotthermometer);
        } else if (latestTemperature < zone.getLowTemperatureThreshold()) {
            myLiveTemperatureImageView.setImageResource(R.drawable.coldthermometer);
        } else {
            myLiveTemperatureImageView.setImageResource(R.drawable.thermometer);
        }
        if (latestHumidity > zone.getHighHumidityThreshold()) {
            myLiveHumidityImageView.setImageResource(R.drawable.water_drop_full);
        } else if (latestHumidity < zone.getLowHumidityThreshold()) {
            myLiveHumidityImageView.setImageResource(R.drawable.water_drop_empty);
        } else {
            myLiveHumidityImageView.setImageResource(R.drawable.water_droplet);
        }


        myGraphView.setTitle("Last 24 Hours");
        myGraphView.setTitleTextSize(60);
        myGraphView.getGridLabelRenderer().setPadding(50);
        //Set bounds and labels for temperature
        myGraphView.getViewport().setYAxisBoundsManual(true);
        myGraphView.getViewport().setMinY(0);
        myGraphView.getViewport().setMaxY(100);
        myGraphView.getGridLabelRenderer().setVerticalLabelsColor(Color.RED);
        myGraphView.getGridLabelRenderer().setVerticalAxisTitle("Temperature (F)");
        myGraphView.getGridLabelRenderer().setVerticalAxisTitleColor(Color.RED);
        myGraphView.getGridLabelRenderer().setVerticalAxisTitleTextSize(50);
        myGraphView.getGridLabelRenderer().setLabelVerticalWidth(20);
        //Set bounds and labels for humidity
        myGraphView.getSecondScale().setMinY(0);
        myGraphView.getSecondScale().setMaxY(100);
        myGraphView.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.BLUE);
        //myGraphView.getGridLabelRenderer().setSecondScaleLabelVerticalWidth(myGraphView.getGridLabelRenderer().getLabelVerticalWidth());
        //Set default X bound for 24 hours from latest reading
        myGraphView.getViewport().setXAxisBoundsManual(true);
        myGraphView.getViewport().setMinX(latestReadingTime - 86400000); //24hrs in millis
        myGraphView.getViewport().setMaxX(latestReadingTime);
        myGraphView.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        myGraphView.getGridLabelRenderer().setHorizontalAxisTitleTextSize(50);
        myGraphView.getGridLabelRenderer().setHorizontalLabelsAngle(45);
        myGraphView.getGridLabelRenderer().setLabelFormatter(
                new DateAsXAxisLabelFormatter(ZoneViewActivity.this,
                        new java.text.SimpleDateFormat("h:mm a")));

        //Make zoomable/scrollable
        myGraphView.getViewport().setScalable(true);
        myGraphView.getViewport().setScalableY(true);



        //TAB 2
        //Set high temperature spinner
        myHighTemperatureThreshold.setSelection(zone.getHighTemperatureThreshold() - MIN_TEMPERATURE_THRESHOLD);
        myHighTemperatureThreshold.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int newThreshold = Integer.parseInt(parent.getItemAtPosition(pos).toString());
                if (newThreshold != zone.getHighTemperatureThreshold()) {
                    zone.setHighTemperatureThreshold(newThreshold);
                    //ALSO SEND UPDATE TO SERVER
                    viewZone(zone);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
        //set low temperature spinner
        myLowTemperatureThreshold.setSelection(zone.getLowTemperatureThreshold() - MIN_TEMPERATURE_THRESHOLD);
        myLowTemperatureThreshold.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int newThreshold = Integer.parseInt(parent.getItemAtPosition(pos).toString());
                if (newThreshold != zone.getLowTemperatureThreshold()) {
                    zone.setLowTemperatureThreshold(newThreshold);
                    //ALSO SEND UPDATE TO SERVER
                    viewZone(zone);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
        //Set high humidity spinner
        myHighHumidityThreshold.setSelection(zone.getHighHumidityThreshold() - MIN_HUMIDITY_THRESHOLD);
        myHighHumidityThreshold.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int newThreshold = Integer.parseInt(parent.getItemAtPosition(pos).toString());
                if (newThreshold != zone.getHighHumidityThreshold()) {
                    zone.setHighHumidityThreshold(newThreshold);
                    //ALSO SEND UPDATE TO SERVER
                    viewZone(zone);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
        //Set low humidity spinner
        myLowHumidityThreshold.setSelection(zone.getLowHumidityThreshold() - MIN_HUMIDITY_THRESHOLD);
        myLowHumidityThreshold.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int newThreshold = Integer.parseInt(parent.getItemAtPosition(pos).toString());
                if (newThreshold != zone.getLowHumidityThreshold()) {
                    zone.setLowHumidityThreshold(newThreshold);
                    //ALSO SEND UPDATE TO SERVER
                    viewZone(zone);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

}
