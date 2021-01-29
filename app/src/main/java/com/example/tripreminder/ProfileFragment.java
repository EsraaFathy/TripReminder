package com.example.tripreminder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    public final String NICK_NAME = "Amin";
    public final String FULL_NAME = "Mahmoud Muhammad Huessein";
    public final String EMAIL = "Mahmoudamin373@gmail.com";
    public final String ADDRESS = "7 El Horria Street, Maadi, Cairo, Egypt";

    public final int UPDATE_NICK_NAME = 1;
    public final int UPDATE_FULL_NAME = 2;
    public final int UPDATE_ADDRESS = 3;


    ImageView profile_image;
    TextView nickNameTxt;
    TextView fullNameTxt;
    TextView emailTxt;
    TextView addressTxt;
    Button logoutBtn;
    Button editNickNameBtn,editFullNameBtn,editAddressBtn;

    Dialog dialog ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: get profile date from shared preference
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

//        super.onViewCreated(view, savedInstanceState);
//        nickNameTxt = view.findViewById(R.id.nicknameTextView);
//        fullNameTxt = view.findViewById(R.id.fullNameTextView);
//        emailTxt = view.findViewById(R.id.emailTextView);
//        addressTxt = view.findViewById(R.id.addressTextView);
//        logoutBtn =  view.findViewById(R.id.logoutBtn);
//
//        editAddressBtn = view.findViewById(R.id.editAddressBtn);
//        editFullNameBtn = view.findViewById(R.id.editFullNameBtn);
//        editNickNameBtn = view.findViewById(R.id.editNickNameBtn);
//        dialog = new Dialog(getContext());
//
//
//        //Todo: set data from shared preference
//        nickNameTxt.setText(NICK_NAME);
//        fullNameTxt.setText(FULL_NAME);
//        emailTxt.setText(EMAIL);
//        addressTxt.setText(ADDRESS);
//
//        logoutBtn.setOnClickListener(v -> {
//            Toast.makeText(getContext(), "Empty shared preference", Toast.LENGTH_SHORT).show();
//        });
//
//        editNickNameBtn.setOnClickListener(v -> {
//            editPopup(UPDATE_NICK_NAME);
//
//        });
//
//        editFullNameBtn.setOnClickListener(v -> {
//            editPopup(UPDATE_FULL_NAME);
//        });
//
//        editAddressBtn.setOnClickListener(v -> {
//            editPopup(UPDATE_ADDRESS);
//        });
    }

    private void editPopup(int edit){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit "+edit);
        final View customLayout = getLayoutInflater().inflate(R.layout.edit_profile, null);
        builder.setView(customLayout);

        EditText popupText = customLayout.findViewById(R.id.textField);

        switch (edit){
            case UPDATE_NICK_NAME:
                popupText.setText(NICK_NAME);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText updatedText = customLayout.findViewById(R.id.textField);
                        if(updatedText.getText().toString().equals("")){
                            Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
                        }
                        sendEditToProfile(updatedText.getText().toString(),UPDATE_NICK_NAME);
                    }
                });

                break;

            case UPDATE_FULL_NAME:
                popupText.setText(FULL_NAME);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText updatedText = customLayout.findViewById(R.id.textField);
                        sendEditToProfile(updatedText.getText().toString(),UPDATE_FULL_NAME);
                    }
                });

                break;

            case UPDATE_ADDRESS:
                popupText.setText(ADDRESS);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText updatedText = customLayout.findViewById(R.id.textField);
                        sendEditToProfile(updatedText.getText().toString(),UPDATE_ADDRESS);
                    }
                });

                break;

        }

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    private void sendEditToProfile(String updatedText,int edit)
    {
        switch (edit){
            case UPDATE_NICK_NAME:
                Toast.makeText(getContext(), "Nick name to : "+updatedText, Toast.LENGTH_SHORT).show();
                break;
            case UPDATE_FULL_NAME:
                Toast.makeText(getContext(), "full name to : "+updatedText, Toast.LENGTH_SHORT).show();
                break;
            case UPDATE_ADDRESS:
                Toast.makeText(getContext(), "address to : "+updatedText, Toast.LENGTH_SHORT).show();
                break;

        }

    }

}