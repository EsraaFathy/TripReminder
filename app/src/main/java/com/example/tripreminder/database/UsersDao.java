package com.example.tripreminder.database;

import com.example.tripreminder.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ValueEventListener;

public class UsersDao {

    public static void addUser(User user, String currentUserId, OnCompleteListener<Void> onCompleteListener){
        DataBase.getUsers().child(currentUserId).child(DataBase.USER_Info_REF)
                .setValue(user)
                .addOnCompleteListener(onCompleteListener);
    }

    public static void addUserTrips(User user, String currentUserId, OnCompleteListener<Void> onCompleteListener){
        DataBase.getUsers().child(currentUserId).child(DataBase.USER_Trip_REF)
                .setValue(user)
                .addOnCompleteListener(onCompleteListener);
    }

    public static void getUserTrips(User user, String currentUserId, OnCompleteListener<Void> onCompleteListener){
        DataBase.getUsers().child(currentUserId).child(DataBase.USER_Trip_REF)
                .setValue(user)
                .addOnCompleteListener(onCompleteListener);
    }

    public static void getUser(String userId, ValueEventListener valueEventListener){
        DataBase.getUsers().child(userId).child(DataBase.USER_Info_REF)
                .addValueEventListener(valueEventListener);

    }

}
