package com.example.graduationproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FriendProfileFragment extends Fragment {
    TextView name,loveNumbers,friendNumbers,location,bio;
    ImageView profile,addLove,addFriend;
    boolean flag=true,flag2=true;
    LinearLayout linearLove,linearAdd;
    String id;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend_profile, container, false);
       // ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        name=view.findViewById(R.id.profile_name);
        loveNumbers=view.findViewById(R.id.profile_love_text);
        friendNumbers=view.findViewById(R.id.profile_friend_text);
        location=view.findViewById(R.id.profile_location);
        bio=view.findViewById(R.id.profile_BIO);
        linearAdd=view.findViewById(R.id.linear_add);
        linearLove=view.findViewById(R.id.linear_love);
        profile=view.findViewById(R.id.profile_image_details);
        addLove=view.findViewById(R.id.profile_add_love);
        addFriend=view.findViewById(R.id.profile_add_friend);
        if(getArguments()!=null){
            id=getArguments().getString("id");

        }
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        getFriendData();

        linearLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==true){
                    addLove.setImageResource(R.drawable.bluelove);
                    flag=false;

                }else {
                    addLove.setImageResource(R.drawable.whitelove);
                    flag=true;

                }
                makeLoves();
            }
        });
        linearAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag2==true){
                    addFriend.setImageResource(R.drawable.blueadd);
                    flag2=false;

                }else {
                    addFriend.setImageResource(R.drawable.whiteadd);
                    flag2=true;

                }
                addFriends();
            }
        });

        

        return view;
    }

    private void getFriendData() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            String  userName=dataSnapshot.child("name").getValue(String.class);
                            String  image=dataSnapshot.child("uri").getValue(String.class);
                            String  userLocation=dataSnapshot.child("location").getValue(String.class);

                            name.setText(userName);
                            location.setText(userLocation);
                            Picasso.get().load(image).into(profile);

                        }

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void makeLoves() {
    }

    private void addFriends() {
    }
}
