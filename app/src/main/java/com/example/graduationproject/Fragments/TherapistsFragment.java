package com.example.graduationproject.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.graduationproject.Activities.MainActivity;
import com.example.graduationproject.Adapter.PagerAdapter;
import com.example.graduationproject.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class TherapistsFragment extends Fragment {

    Toolbar toolbar;
    EditText therapySearch;
    TabLayout tabLayout;
    TabItem therapyByName,therapyByLocation;
    PagerAdapter pagerAdapter;
    ViewPager viewPager;
    ImageView backImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_therapists, container, false);

        viewPager = v.findViewById(R.id.view_pager);
        tabLayout = v.findViewById(R.id.therapists_tab_layout);
        toolbar = v.findViewById(R.id.therapists_tool_bar);
        therapySearch = v.findViewById(R.id.search_therapists);
        therapyByName = v.findViewById(R.id.therapists_byName_tab_item);
        therapyByLocation = v.findViewById(R.id.therapists_byLocation_tab_item);
        backImage = v.findViewById(R.id.back);


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // move from fragment to activity 
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        pagerAdapter = new PagerAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition()==0){
                    pagerAdapter.notifyDataSetChanged();
                }else if (tab.getPosition()==1){
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));




        return v;
    }
}