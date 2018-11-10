package ssu.groupname.baseapplication.utils;

import com.google.gson.Gson;

import ssu.groupname.baseapplication.models.User;

public class UserParser {

    public static User userFromJson (String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, User.class);
    }
}
