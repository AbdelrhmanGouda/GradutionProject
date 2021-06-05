package com.example.graduationproject.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.graduationproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class


ProfileFragment extends Fragment implements View.OnClickListener {
    private TextView profileName,appointmentsNum,friendsNum,heartNum;
    private ImageView profilePic;
    private FloatingActionButton floatingEditProfileBtn ,delete;
    public FirebaseAuth auth ;
    String userName,userImage,friendsNumber,heartNumber,appointmentsNumber, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(false);
        profileName=view.findViewById(R.id.profile_name);
        appointmentsNum=view.findViewById(R.id.profile_therapist_num);
        friendsNum=view.findViewById(R.id.profile_friends_num);
        heartNum=view.findViewById(R.id.profile_heart_num);
        profilePic=view.findViewById(R.id.profile_image);
        floatingEditProfileBtn =view.findViewById(R.id.floating_edit_profile_btn);
        floatingEditProfileBtn.setOnClickListener(this);
        delete =view.findViewById(R.id.deleteBtn);
        auth=FirebaseAuth.getInstance();
        dataBase();


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                Log.d("123id", uid);

                //re-auth user
                user = FirebaseAuth.getInstance().getCurrentUser();

                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,
                // such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
                //GoogleAuthProvider.getCredential(,null);
               // FacebookAuthProvider.getCredential();

                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Log.d("123re-auth", "User re-authenticated.");
                                ///////////////////////////////////////////
                                /// realtime
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("Users");
                                myRef.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("123real", "User account deleted.");
                                        }
                                    }
                                });

                                ////storage  // Create a storage reference from our app// Defining the child of storageReference
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                final StorageReference ref = storageReference.child("images/").child(uid + "/UserImage");
                                // Delete the file
                                ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // File deleted successfully
                                        Log.d("123storage", "User account deleted.");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Uh-oh, an error occurred!
                                    }
                                });

                                ///auth//////////////////////////////////////
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("123auth", "User account deleted.");
                                            // startActivity(new Intent(getActivity(), SignMainActivity.class));
                                        } else {
                                            Log.w("123auth","Something is wrong!");
                                        }
                                    }
                                });
                                ///////////////////////////////////////
                            }
                        })
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                ///auth//////////////////////////////////////
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("123auth2", "User account deleted.");
                                            // startActivity(new Intent(getActivity(), SignMainActivity.class));
                                        } else {
                                            Log.w("123auth2","Something is wrong!");
                                        }
                                    }
                                });
                                ///////////////////////////////////////
                                Log.w("123re-authOnComplete2","Something is wrong!");
                            }
                        });


            }
        });

        return view;
    }

    private void dataBase() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        final String id =firebaseUser.getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!= null){
                    if (snapshot.exists()&& snapshot.getChildrenCount()>0&&snapshot.getValue().toString().length()>0){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            userName=snapshot.child("name").getValue(String.class);
                            userImage=snapshot.child("uri").getValue(String.class);
                            profileName.setText(userName);
                            Picasso.get().load(userImage).into(profilePic);
                            password =snapshot.child("pass").getValue(String.class);
                            Log.i(password, "old password: ");
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query query1 = FirebaseDatabase.getInstance().getReference().child("Friends").child("FriendsNumber").child(id);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!= null){
                    if (snapshot.exists()&& snapshot.getChildrenCount()>0&&snapshot.getValue().toString().length()>0){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            friendsNumber=snapshot.child("Friends").getValue(String.class);
                            friendsNum.setText(friendsNumber);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query query2 = FirebaseDatabase.getInstance().getReference().child("Profiles").child("Appreciate").child("Likes").child(id);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!= null){
                    if (snapshot.exists()&& snapshot.getChildrenCount()>0&&snapshot.getValue().toString().length()>0){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            heartNumber=snapshot.child("Appreciate").getValue(String.class);
                            heartNum.setText(heartNumber);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query query3 = FirebaseDatabase.getInstance().getReference().child("Profiles").child("appoints number").child(id);
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!= null){
                    if (snapshot.exists()&& snapshot.getChildrenCount()>0&&snapshot.getValue().toString().length()>0){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            appointmentsNumber=snapshot.child("appointments").getValue(String.class);
                            appointmentsNum.setText(appointmentsNumber);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,new EditProfileFragment()).addToBackStack("").commit();

    }



}