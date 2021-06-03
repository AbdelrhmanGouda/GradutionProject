package com.example.graduationproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Adapter.TherapistsByNameAdapter;
import com.example.graduationproject.Data.TherapistsByNameData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TherapistsByLocationFragment extends Fragment {


    RecyclerView recyclerView;
    List<TherapistsByNameData> therapistsDataList;
    TherapistsByNameAdapter therapistsAdapter;
    DatabaseReference therapistsRef;
    FirebaseAuth auth;
    String docLocation,userLocation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_therapists_by_location, container, false);
        recyclerView = view.findViewById(R.id.therapists_by_location_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        therapistsDataList = new ArrayList<>();
            location();
            database();
        return view;
    }
    private void database(){
        therapistsRef=FirebaseDatabase.getInstance().getReference().child("Doctors");
        Query query = FirebaseDatabase.getInstance().getReference("Doctors").orderByChild("location").equalTo(userLocation);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!= null) {
                    if (snapshot.exists() && snapshot.getChildrenCount() > 0 && snapshot.getValue().toString().length() > 0) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            TherapistsByNameData model = dataSnapshot.getValue(TherapistsByNameData.class);
                            therapistsDataList.add(model);
                        }

                        therapistsAdapter = new TherapistsByNameAdapter(getContext(), therapistsDataList);
                        recyclerView.setAdapter(therapistsAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void location(){
        auth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        Query query =FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!= null){
                    if (snapshot.exists()&& snapshot.getChildrenCount()>0&&snapshot.getValue().toString().length()>0){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            userLocation=snapshot.child("location").getValue(String.class);

                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}