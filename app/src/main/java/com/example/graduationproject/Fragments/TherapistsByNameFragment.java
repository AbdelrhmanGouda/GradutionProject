package com.example.graduationproject.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationproject.Adapter.TherapistsByNameAdapter;
import com.example.graduationproject.R;

import java.util.ArrayList;
import java.util.List;


public class TherapistsByNameFragment extends Fragment {

    RecyclerView therapists_byName_recyclerview;
    private TherapistsByNameAdapter adapter;
    private List<String> list;
    String n1[],l2[];



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_therapists_by_name, container, false);

        n1 = getResources().getStringArray(R.array.names);
        l2 = getResources().getStringArray(R.array.description);
        list = new ArrayList<>();
        adapter = new TherapistsByNameAdapter(getContext(),n1,l2);
        therapists_byName_recyclerview = v.findViewById(R.id.therapists_byName_recyclerview);
        therapists_byName_recyclerview.setAdapter(adapter);
        therapists_byName_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));


        return v;
    }
}