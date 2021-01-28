package com.example.tripreminder.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataBase {

    static FirebaseDatabase dp=FirebaseDatabase.getInstance();
    static String USER_REF="Users";
    static String USER_Info_REF="Info";
    static String USER_Trip_REF="Trips";
    static String GROUP_REF="Groups";
    public static DatabaseReference getUsers(){
        return dp.getReference().child(USER_REF).child(USER_Info_REF);
    }
    public static DatabaseReference getUsersTrips(){
        return dp.getReference().child(USER_REF).child(USER_Trip_REF);
    }
    public static DatabaseReference getGroups(){
        return dp.getReference().child(GROUP_REF);
    }
    public static DatabaseReference getMessages(String groupName){
        return dp.getReference().child(GROUP_REF).child(groupName).child("Messages");
    }

}
