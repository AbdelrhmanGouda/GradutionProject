package com.example.graduationproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationproject.Adapter.TherapistsByNameAdapter;
import com.example.graduationproject.Data.TherapistsByNameData;
import com.example.graduationproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TherapistsByNameFragment extends Fragment {



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public TherapistsByNameFragment() {

    }


    public static TherapistsByNameFragment newInstance(String param1, String param2) {
        TherapistsByNameFragment fragment = new TherapistsByNameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView recyclerView;
    List<TherapistsByNameData> therapistsDataList;
    TherapistsByNameAdapter therapistsAdapter;
    DatabaseReference therapistsRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_therapists_by_name, container, false);

        recyclerView = view.findViewById(R.id.therapists_by_name_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        therapistsDataList = new ArrayList<>();

        therapistsRef = FirebaseDatabase.getInstance().getReference("Doctors");
        therapistsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    TherapistsByNameData model = dataSnapshot.getValue(TherapistsByNameData.class);
                    therapistsDataList.add(model);
                }
                therapistsAdapter = new TherapistsByNameAdapter(getContext(),therapistsDataList);
                recyclerView.setAdapter(therapistsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}