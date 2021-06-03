package com.example.graduationproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.graduationproject.Adapter.AppointmentsPagerAdapter;
import com.example.graduationproject.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class AppointmentsFragment extends Fragment {
    private TabLayout appointmentsTabLayout;
    private TabItem allTab, upcomingTab;
    private AppointmentsPagerAdapter appointmentsPagerAdapter;
    private ViewPager appointmentsViewPager;
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_appointments, container, false);
         appointmentsViewPager = view.findViewById(R.id.appointments_view_pager);
         appointmentsTabLayout = view.findViewById(R.id.appointments_tab_layout);
         allTab = view.findViewById(R.id.all_tab_item);
         upcomingTab = view.findViewById(R.id.upcoming_tab_item);

         appointmentsPagerAdapter=new AppointmentsPagerAdapter(getFragmentManager(),appointmentsTabLayout.getTabCount());
         appointmentsViewPager.setAdapter(appointmentsPagerAdapter);

         appointmentsTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
             @Override
             public void onTabSelected(TabLayout.Tab tab) {
                 appointmentsViewPager.setCurrentItem(tab.getPosition());
                 if (tab.getPosition()==0){
                     appointmentsPagerAdapter.notifyDataSetChanged();
                 }else if (tab.getPosition()==1){
                     appointmentsPagerAdapter.notifyDataSetChanged();
                 }
             }

             @Override
             public void onTabUnselected(TabLayout.Tab tab) {

             }

             @Override
             public void onTabReselected(TabLayout.Tab tab) {

             }
         });
         appointmentsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(appointmentsTabLayout));
         return view;
     }
}