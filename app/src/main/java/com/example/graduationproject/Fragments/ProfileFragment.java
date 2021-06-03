package com.example.graduationproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.graduationproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class


ProfileFragment extends Fragment implements View.OnClickListener {
    private TextView profileName,appointmentsNum,friendsNum,heartNum;
    private ImageView profilePic;
    private FloatingActionButton floatingEditProfileBtn;
    public FirebaseAuth auth ;
    String userName,userImage,friendsNumber,heartNumber,appointmentsNumber;

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
        auth=FirebaseAuth.getInstance();
        dataBase();

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