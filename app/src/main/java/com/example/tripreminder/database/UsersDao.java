package com.example.tripreminder.database;

import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class UsersDao {

    public static void addUser(User user, String currentUserId, OnCompleteListener<Void> onCompleteListener){
        DataBase.getUsers().child(currentUserId).child(DataBase.USER_Info_REF)
                .setValue(user)
                .addOnCompleteListener(onCompleteListener);
    }

    public static void addUserTrips(List<TripTable> trips, String currentUserId, OnCompleteListener<Void> onCompleteListener){
        DataBase.getUsers().child(currentUserId).child(DataBase.USER_Trip_REF).setValue(Arrays.asList(trips))
                .addOnCompleteListener(onCompleteListener);
    }

    public static void getUserTrips(String currentUserId, ValueEventListener valueEventListener){
        DataBase.getUsers().child(currentUserId).child(DataBase.USER_Trip_REF).child(DataBase.USER_Trip_branch)
                .addValueEventListener(valueEventListener);
    }

    public static void getUser(String userId, ValueEventListener valueEventListener){
        DataBase.getUsers().child(userId).child(DataBase.USER_Info_REF)
                .addValueEventListener(valueEventListener);

    }

}
