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
import com.example.graduationproject.Adapter.NotificationTabAdapter;
import com.example.graduationproject.Data.AllTabData;
import com.example.graduationproject.Data.NotificationTabData;
import com.example.graduationproject.R;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsAllTabFragment extends Fragment {
    private RecyclerView allTabRecyclerView;
    private List<AllTabData> allTabDataList;
    private AllTabAdapter allTabAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointments_all_tab, container, false);
        allTabRecyclerView=view.findViewById(R.id.all_tab_recycler_view);
        allTabRecyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        allTabRecyclerView.setHasFixedSize(true);
        allTabDataList =new ArrayList<>();
        allTabDataList.add(new AllTabData("26 1th 2021","11:13","Upcoming","Dr.ahmed"));
        allTabDataList.add(new AllTabData("26 1th 2021","11:13","Canceled","Dr.ahmed"));
        allTabDataList.add(new AllTabData("26 1th 2021","11:13","Upcoming","Dr.ahmed"));
        allTabDataList.add(new AllTabData("26 1th 2021","11:13","Upcoming","Dr.ahmed"));
        allTabDataList.add(new AllTabData("26 1th 2021","11:13","Upcoming","Dr.ahmed"));
        allTabDataList.add(new AllTabData("26 1th 2021","11:13","Upcoming","Dr.ahmed"));
        allTabAdapter =new AllTabAdapter(allTabDataList,getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
        allTabRecyclerView.addItemDecoration(dividerItemDecoration);
        allTabRecyclerView.setAdapter(allTabAdapter);
        return view;
    }
}