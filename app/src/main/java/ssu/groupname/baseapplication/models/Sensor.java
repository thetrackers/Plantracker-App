package ssu.groupname.baseapplication.models;

import java.util.List;

public class Sensor {
    //Name & ID
    private String mySensorName;
    private int mySensorId;
    //private List<Threshold> myThresholds; //individual sensor thresholds?

    //List of Readings
    private List<Reading> myReadings;

    //Getters
    public String getName() {return mySensorName;}
    public int getId() {return mySensorId;}
    public List<Reading> getReadings() {return myReadings;}
}
