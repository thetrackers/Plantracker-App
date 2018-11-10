package ssu.groupname.baseapplication.models;

import java.util.List;

public class User {
    //Name & ID, other info
    private String myFirstName;
    private String myLastName;
    private int myUserId;
    private int myTelephoneNumber;  //telephone number
    //private int myPassword; //no security right now

    //List of Gardens
    private List<Garden> myGardens;

    //Getters
    public String getFirstName() {return myFirstName;}
    public String getLastName() {return myLastName;}
    public int getId() {return myUserId;}
    public int getTelephoneNumber() {return myTelephoneNumber;}
    public List<Garden> getGardens() {return myGardens;}
}
