package com.example.graduationproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Adapter.AllTabAdapter;
import com.example.graduationproject.Adapter.TestAdapter;
import com.example.graduationproject.Data.AllTabData;
import com.example.graduationproject.Data.TestData;
import com.example.graduationproject.R;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment {
    private RecyclerView testRecyclerView;
    private List<TestData> testDataList;
    private TestAdapter testAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        testRecyclerView=view.findViewById(R.id.test_recycler_view);
        int numberOfColumns = 2;
        testRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),numberOfColumns));
        testRecyclerView.setHasFixedSize(true);
        testDataList =new ArrayList<>();
        testDataList.add(new TestData("Depression",R.drawable.deppression));
        testDataList.add(new TestData("Alcohol Addiction",R.drawable.alcohol));
        testDataList.add(new TestData("Behavioral Addiction",R.drawable.behavioral));
        testDataList.add(new TestData("Drug Addiction",R.drawable.drug));
        testDataList.add(new TestData("Anixiety",R.drawable.anxiety));
        testDataList.add(new TestData("Stress",R.drawable.stress));
        testDataList.add(new TestData("Psychosexual Dysfunction",R.drawable.psychosexual));
        testDataList.add(new TestData("Psychosexual Dysfunction",R.drawable.adhd));
        testDataList.add(new TestData("Autism",R.drawable.autism));
        testDataList.add(new TestData("Pseudobulbar Affect (PBA)",R.drawable.pseudobullar));
        testDataList.add(new TestData("Bullying",R.drawable.bullying));
        testDataList.add(new TestData("Schizophrenia",R.drawable.schizophrenia));
        testDataList.add(new TestData("Dissociative Identity Disorder (DID)",R.drawable.dissociative));
        testDataList.add(new TestData("Borderline personality disorder (BPD)",R.drawable.border));
        testDataList.add(new TestData("low self esteem",R.drawable.low_self));
        testDataList.add(new TestData("Bipolar Disorder",R.drawable.bipolar));
        testDataList.add(new TestData("Mania",R.drawable.mania));
        testDataList.add(new TestData("Panic Disorder",R.drawable.panic));
        testDataList.add(new TestData("Hoarding Disorder",R.drawable.hoarding));
        testDataList.add(new TestData("Psychosis",R.drawable.psychosis));
        testDataList.add(new TestData("Social Anxiety Disorder",R.drawable.social));
        testDataList.add(new TestData("Imposter syndrome",R.drawable.imposter));
        testDataList.add(new TestData("Obsessive-Compulsive Disorder (OCD)",R.drawable.ocd));
        testDataList.add(new TestData("Eating Disorder",R.drawable.eating));
        testDataList.add(new TestData("Narcissistic Personality Disorder",R.drawable.narcissistic));
        testDataList.add(new TestData("Empathy Deficit Disorder",R.drawable.empathy));
        testDataList.add(new TestData("Illness anxiety disorder",R.drawable.illness));
        testDataList.add(new TestData("Posttraumatic stress disorder (PTSD)",R.drawable.ptsd));
        testAdapter =new TestAdapter(testDataList,getContext());
        testRecyclerView.setAdapter(testAdapter);

        return view;
    }
}