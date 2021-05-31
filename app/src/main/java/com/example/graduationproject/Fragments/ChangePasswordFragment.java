package com.example.graduationproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

  EditText currentPassword,newPassword,confirmPassword;

  Button changePasswordBtn;
  FirebaseAuth auth;
  String id ,oldPassword,currentPasswordTxt,newPasswordTxt,confirmPasswordTxt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        currentPassword=view.findViewById(R.id.current_password);
        newPassword=view.findViewById(R.id.new_password);
        confirmPassword=view.findViewById(R.id.confirm_new_password);
        changePasswordBtn=view.findViewById(R.id.change_password_btn);
        auth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =auth.getCurrentUser();
        id = firebaseUser.getUid();
        dataBase();
        changePasswordBtn.setOnClickListener(this);

        return view;
    }

    private void dataBase(){
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                if(snapshot!= null){
                    if (snapshot.exists()&& snapshot.getChildrenCount()>0&&snapshot.getValue().toString().length()>0){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            oldPassword=snapshot.child("pass").getValue(String.class);
                            Log.i(oldPassword, "old password: ");
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // method to reset the password with a link sent to the email
    /*
    private void resetPassword(){
        String email = confirmPassword.getText().toString();
        if (email.isEmpty()){
            confirmPassword.setError("Email is required!");
            confirmPassword.requestFocus();
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            confirmPassword.setError("Enter a valid email please!");
            confirmPassword.requestFocus();
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "check your email to reset your password", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "try again! something wrong happened!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

     */
    /*
    private void changePassword(){

        currentPasswordTxt=currentPassword.getText().toString();
        newPasswordTxt=newPassword.getText().toString();
        confirmPasswordTxt=confirmPassword.getText().toString();
        Log.i(currentPasswordTxt, "lllllllllllllllllllllllllll");
        Log.i(oldPassword, "hhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        if (currentPasswordTxt.equals(oldPassword) ){
            Log.i(currentPasswordTxt, "kkkkkkkkkkkkkkkkk");
            Log.i(oldPassword, "ppppppppppppp");

            if (newPasswordTxt.equals(confirmPasswordTxt)){
                Query query1 =FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot!= null){
                            if (snapshot.exists()&& snapshot.getChildrenCount()>0&&snapshot.getValue().toString().length()>0){
                                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                    snapshot.getRef().child("pass").setValue(newPasswordTxt);
                                    Toast.makeText(getContext(), "Password is changed", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else {
                Toast.makeText(getContext(), "You must enter the same password to confirm it ", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getContext(), "Enter valid password and try again  ", Toast.LENGTH_LONG).show();
            Log.i(currentPasswordTxt, "nnnnnnnnnnnnn");
            Log.i(oldPassword, "mmmmmmmmmmmmmmmmm");
        }

    }


     */

    private void changePasswordNew(){
        oldPassword=currentPassword.getText().toString();
        newPasswordTxt=newPassword.getText().toString();
        confirmPasswordTxt=confirmPassword.getText().toString();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(),oldPassword);
        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                user.updatePassword(newPasswordTxt).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Password Is Updated...", Toast.LENGTH_SHORT).show();
                        currentPassword.getText().clear();
                        newPassword.getText().clear();
                        confirmPassword.getText().clear();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View view) {

       // resetPassword();
      //  changePassword();
        changePasswordNew();

    }


}