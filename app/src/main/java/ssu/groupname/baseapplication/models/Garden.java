package ssu.groupname.baseapplication.models;

import java.util.List;

public class Garden {
    //Name & ID
    private String myGardenName;
    private int myGardenId;

    //List of Zones
    private List<Zone> myZones;

    //Getters
    public String getName() {return myGardenName;}
    public int getId() {return myGardenId;}
    public List<Zone> getZones() {return myZones;}
}
