package com.example.graduationproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationproject.Adapter.AllTabAdapter;
import com.example.graduationproject.Data.AllTabData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsUpcomingTabFragment extends Fragment {
  private RecyclerView upcomingTabRecyclerView;
    private List<AllTabData> upcomingTabDataList;
    private AllTabAdapter allTabAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view =inflater.inflate(R.layout.fragment_appointments_upcoming_tab, container, false);
       upcomingTabRecyclerView=view.findViewById(R.id.upcoming_tab_recycler_view);
        upcomingTabRecyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        upcomingTabRecyclerView.setHasFixedSize(true);
        upcomingTabDataList =new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference("request appointment").orderByChild("state").equalTo("Upcoming");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    if (snapshot.exists() && snapshot.getChildrenCount() > 0 && snapshot.getValue().toString().length() > 0) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                         upcomingTabDataList.clear();
                            AllTabData allTabData = snapshot1.getValue(AllTabData.class);
                            upcomingTabDataList.add(allTabData);
                        }
                        allTabAdapter =new AllTabAdapter(upcomingTabDataList,getContext());
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
                        upcomingTabRecyclerView.addItemDecoration(dividerItemDecoration);
                        upcomingTabRecyclerView.setAdapter(allTabAdapter);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


       return view;
    }
}