package com.example.tripreminder.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataBase {

    static FirebaseDatabase dp=FirebaseDatabase.getInstance();
    public static String USER_REF="Users";
    public static String USER_Info_REF="Info";
    public static String USER_Trip_REF="Trips";
    public static String USER_Trip_branch="0";
    public static DatabaseReference getUsers(){
        return dp.getReference().child(USER_REF);
    }


}
