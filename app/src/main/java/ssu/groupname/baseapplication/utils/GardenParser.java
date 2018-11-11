package ssu.groupname.baseapplication.utils;

import com.google.gson.Gson;

import ssu.groupname.baseapplication.models.Garden;

public class GardenParser {

    public static Garden gardenFromJson (String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Garden.class);
    }
}
