package com.example.graduationproject.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.Sign.CustomToast;
import com.example.graduationproject.Sign.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {
    private ImageView editImage;
    private EditText editName, editEmail, editLocation, editPhone;
    private Button updateBtn;
    private FloatingActionButton floatingEditImageBtn;
    public FirebaseAuth auth ;
    String userImage,userName,userEmail,userLocation,userPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setHasOptionsMenu(false);
        editImage =view.findViewById(R.id.edit_profile_image);
        floatingEditImageBtn=view.findViewById(R.id.floating_edit_image_btn);
        editName =view.findViewById(R.id.edit_name);
        editEmail =view.findViewById(R.id.edit_email);
        editLocation =view.findViewById(R.id.edit_location);
        editPhone =view.findViewById(R.id.edit_phone);
        updateBtn =view.findViewById(R.id.edit_profile_update_btn);
        auth = FirebaseAuth.getInstance();
        dataBase();


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        return view;
    }
    private void dataBase() {
        FirebaseUser firebaseUser =auth.getCurrentUser();
        final String id = firebaseUser.getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!= null){
                    if (snapshot.exists()&& snapshot.getChildrenCount()>0&&snapshot.getValue().toString().length()>0){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            userName=snapshot.child("name").getValue(String.class);
                            userImage=snapshot.child("uri").getValue(String.class);
                            userEmail=snapshot.child("email").getValue(String.class);
                            userLocation=snapshot.child("location").getValue(String.class);
                            userPhone=snapshot.child("phone").getValue(String.class);
                            editName.setText(userName);
                            editEmail.setText(userEmail);
                            editLocation.setText(userLocation);
                            editPhone.setText(userPhone);
                            Picasso.get().load(userImage).into(editImage);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateData() {
        FirebaseUser firebaseUser =auth.getCurrentUser();
        final String id = firebaseUser.getUid();
        final String name = editName.getText().toString();
        final String email = editEmail.getText().toString();
        final String location = editLocation.getText().toString();
        final String phone = editPhone.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!= null){
                    if (snapshot.exists()&& snapshot.getChildrenCount()>0&&snapshot.getValue().toString().length()>0){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            snapshot.getRef().child("name").setValue(name);
                            snapshot.getRef().child("email").setValue(email);
                            snapshot.getRef().child("location").setValue(location);
                            snapshot.getRef().child("phone").setValue(phone);
                            Toast.makeText(getContext(), "update", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void checkValidation() {

        // Get all edittext texts
        String getFullName = editName.getText().toString();
        String getEmailId = editEmail.getText().toString();
        String getMobileNumber = editPhone.getText().toString();
        String getLocation = editLocation.getText().toString();
        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
           ) {
            Toast.makeText(getContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
        }

        // Check if email id valid or not
        else if (!m.find()) {
            Toast.makeText(getContext(), "Your Email Id is Invalid.", Toast.LENGTH_SHORT).show();
        }
        else if (editPhone.length() != 11)
        {
            Toast.makeText(getContext(), "Mobile should be 11 Numbers", Toast.LENGTH_SHORT).show();
        }
        else {
            updateData();
        }
    }

}