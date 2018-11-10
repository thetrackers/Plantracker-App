package ssu.groupname.baseapplication.models;

import java.sql.Timestamp;

public class Reading {
    //Info
    private int mySensorId;  //necessary in sending reading to server,
                            // but not in receiving?
    //private Timestamp myTimestamp;
    private String myTimestamp; //FOR NOW using String for Timestamp
    private int myTemperature;
    private int myHumidity;

    //Getters
    public String getTimestamp() {return myTimestamp;}
    public int getTemperature() {return myTemperature;}
    public int getHumidity() {return myHumidity;}
}
