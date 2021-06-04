package com.example.graduationproject.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Adapter.AllTabAdapter;
import com.example.graduationproject.Data.AllTabData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;

public class AppointmentsAllTabFragment extends Fragment {
    private RecyclerView allTabRecyclerView;
    private List<AllTabData> allTabDataList;
    private AllTabAdapter allTabAdapter;
    String  dayDate, time,state,id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments_all_tab, container, false);
        allTabRecyclerView = view.findViewById(R.id.all_tab_recycler_view);
        allTabRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allTabRecyclerView.setHasFixedSize(true);
        allTabDataList = new ArrayList<>();

       Query query = FirebaseDatabase.getInstance().getReference().child("appointment")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    if (snapshot.exists() && snapshot.getChildrenCount() > 0 && snapshot.getValue().toString().length() > 0) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            dayDate = snapshot1.child("dayDate").getValue(String.class);
                            time = snapshot1.child("endTime").getValue(String.class);
                            state=snapshot1.child("state").getValue(String.class);
                            id=snapshot1.child("id").getValue(String.class);
                        }
                     isConfirmed(dayDate, time,state,id);
                        getData();

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void getData() {
            Query query1 = FirebaseDatabase.getInstance().getReference().child("appointment")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    allTabDataList.clear();
                    if (snapshot.exists() && snapshot.getChildrenCount() > 0 && snapshot.getValue().toString().length() > 0){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                            AllTabData allTabData = snapshot1.getValue(AllTabData.class);
                            allTabDataList.add(allTabData);
                        }
                        allTabAdapter = new AllTabAdapter(allTabDataList, getContext());
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
                        allTabRecyclerView.addItemDecoration(dividerItemDecoration);
                        allTabAdapter.notifyDataSetChanged();
                        allTabRecyclerView.setAdapter(allTabAdapter);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    private void isConfirmed(String oldDate, String oldTime ,String oldState,String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Time dtNow = new Time();
        dtNow.setToNow();
        int hours = dtNow.hour;
        String currentTime = dtNow.format("%H:%M");
      String currentDate = dtNow.format("%d,%m,%y");
        if (!oldState.equals("Canceled")){
            if (currentDate.compareTo(oldDate.substring(4)) < 0){
                databaseReference.child("appointment").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id).child("state").setValue("Upcoming");
            }
            else if (currentDate.compareTo(oldDate.substring(4)) == 0) {
                    if (currentTime.compareTo(oldTime) < 0) {
                        databaseReference.child("appointment").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id).child("state").setValue("Upcoming");
                    } else {
                        databaseReference.child("appointment").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id).child("state").setValue("Over");
                    }
                }
           else  {

                databaseReference.child("appointment").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id).child("state").setValue("Over");

           }
        }
    }
}