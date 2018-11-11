package ssu.groupname.baseapplication.models;

import java.sql.Timestamp;

public class Reading {
    //Info
    private int mySensorId;  //necessary in sending reading to server,
                            // but not in receiving?
    //private Timestamp myTimestamp;
    private long myTimestamp; //Milliseconds since Jan 1 1970, according to Java Date standard
    private int myTemperature;
    private int myHumidity;

    //Getters
    public long getTimestamp() {return myTimestamp;}
    public int getTemperature() {return myTemperature;}
    public int getHumidity() {return myHumidity;}
}
