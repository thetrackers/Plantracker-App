package ssu.groupname.baseapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

//In order to pass this class via intent.putExtra, the class must be either
//serializable, or parcelable.  Serialization is SLOW so we implement parcelable
//You cannot just pass object references via intent because different activities
//run on different processes with different memory spaces
public class Garden implements Parcelable {
    //Name & ID
    private String myGardenName;
    private int myGardenId;

    //List of Zones
    private List<Zone> myZones;

    //Getters
    public String getName() {return myGardenName;}
    public int getId() {return myGardenId;}
    public List<Zone> getZones() {return myZones;}


    //Must implement this method to implement Parcelable
    @Override
    public int describeContents() {
        return 0; //a bitmask for if our class has child classes
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(myGardenName);
        parcel.writeInt(myGardenId);
        parcel.writeList(myZones);
    }

    public static final Parcelable.Creator<Garden> CREATOR = new Parcelable.Creator<Garden>() {
        public Garden createFromParcel(Parcel parcel) {
            return new Garden(parcel);
        }
        public Garden[] newArray(int size){
            return new Garden[size];
        }
    };

    private Garden(Parcel parcel) {
        myGardenName = parcel.readString();
        myGardenId = parcel.readInt();
        //myZones = parcel.readList();
    }
}
