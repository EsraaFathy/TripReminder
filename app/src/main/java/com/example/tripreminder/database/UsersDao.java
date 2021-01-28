package com.example.tripreminder.database;

import com.example.tripreminder.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ValueEventListener;

public class UsersDao {

    public static void addUser(User user, String currentUserId, OnCompleteListener<Void> onCompleteListener){
        DataBase.getUsers().child(currentUserId)
                .setValue(user)
                .addOnCompleteListener(onCompleteListener);
    }
    public static void getUser(String userId, ValueEventListener valueEventListener){
        DataBase.getUsers().child(userId)
                .addValueEventListener(valueEventListener);

    }

}
