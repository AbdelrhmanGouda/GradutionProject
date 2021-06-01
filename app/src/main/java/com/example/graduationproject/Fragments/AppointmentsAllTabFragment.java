package com.example.graduationproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationproject.Adapter.AllTabAdapter;
import com.example.graduationproject.Adapter.NotificationTabAdapter;
import com.example.graduationproject.Data.AllTabData;
import com.example.graduationproject.Data.NotificationTabData;
import com.example.graduationproject.Notification.Data;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentsAllTabFragment extends Fragment {
    private RecyclerView allTabRecyclerView;
    private List<AllTabData> allTabDataList;
    private AllTabAdapter allTabAdapter;
    String conf, dayDate, time,state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments_all_tab, container, false);
        allTabRecyclerView = view.findViewById(R.id.all_tab_recycler_view);
        allTabRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allTabRecyclerView.setHasFixedSize(true);
        allTabDataList = new ArrayList<>();
        Query query = FirebaseDatabase.getInstance().getReference().child("request appointment")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    if (snapshot.exists() && snapshot.getChildrenCount() > 0 && snapshot.getValue().toString().length() > 0) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            conf = snapshot.child("requestStatus").getValue(String.class);
                            dayDate = snapshot.child("dayDate").getValue(String.class);
                            time = snapshot.child("endTime").getValue(String.class);
                            state=snapshot.child("state").getValue(String.class);
                        }
                        isConfirmed(dayDate, time,state);
                        getData(conf);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void getData(String confirm) {
        if (confirm.equals("confirmed")) {
            Query query1 = FirebaseDatabase.getInstance().getReference().child("request appointment")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            allTabDataList.clear();
                            AllTabData allTabData = snapshot.getValue(AllTabData.class);
                            allTabDataList.add(allTabData);

                        }
                        allTabAdapter = new AllTabAdapter(allTabDataList, getContext());
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
                        allTabRecyclerView.addItemDecoration(dividerItemDecoration);
                        allTabRecyclerView.setAdapter(allTabAdapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void isConfirmed(String oldDate, String oldTime ,String oldState) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String currentDate = new SimpleDateFormat("EEE dd,MM,yy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm a", Locale.getDefault()).format(new Date());
        if (!state.equals("Canceled")){
            if (currentDate.compareTo(oldDate) <= 0) {
                if (currentTime.compareTo(oldTime) < 0) {
                    databaseReference.child("request appointment").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("state").setValue("Upcoming");
                } else {
                    databaseReference.child("request appointment").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("state").setValue("Over");
                }
            }
           else  {
                databaseReference.child("request appointment").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("state").setValue("Over");
            }
        }
    }
}