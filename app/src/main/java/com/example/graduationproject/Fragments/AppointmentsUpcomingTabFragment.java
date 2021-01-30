package com.example.graduationproject.Fragments;

import android.os.Bundle;

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
        upcomingTabDataList.add(new AllTabData("26 1th 2021","11:13","Upcoming","Dr.ahmed"));
        upcomingTabDataList.add(new AllTabData("26 1th 2021","11:13","Canceled","Dr.ahmed"));
        upcomingTabDataList.add(new AllTabData("26 1th 2021","11:13","Upcoming","Dr.ahmed"));
        allTabAdapter =new AllTabAdapter(upcomingTabDataList,getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
        upcomingTabRecyclerView.addItemDecoration(dividerItemDecoration);
        upcomingTabRecyclerView.setAdapter(allTabAdapter);
       return view;
    }
}