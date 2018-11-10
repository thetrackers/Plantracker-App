package ssu.groupname.baseapplication.models;

import java.util.List;

public class Zone {
    //Name & ID, other info
    private String myZoneName;
    private int myZoneId;
    private int myHighTemperatureThreshold;
    private int myLowTemperatureThreshold;
    private int myHighHumidityThreshold;
    private int myLowHumidityThreshold;

    //List of Sensors
    private List<Sensor> mySensors;

    //Getters
    public String getName() {return myZoneName;}
    public int getId() {return myZoneId;}
    public int getHighTemperatureThreshold() {return myHighTemperatureThreshold;}
    public int getLowTemperatureThreshold() {return myLowTemperatureThreshold;}
    public int getHighHumidityThreshold() {return myHighHumidityThreshold;}
    public int getLowHumidityThreshold() {return myLowHumidityThreshold;}
    public List<Sensor> getSensors() {return mySensors;}
}
