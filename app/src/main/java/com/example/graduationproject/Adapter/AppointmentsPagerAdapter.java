package com.example.graduationproject.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.graduationproject.Fragments.AppointmentsAllTabFragment;
import com.example.graduationproject.Fragments.AppointmentsUpcomingTabFragment;

public class AppointmentsPagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    public AppointmentsPagerAdapter(@NonNull FragmentManager fm , int numOfTabs) {
        super(fm);
        this.numOfTabs=numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AppointmentsAllTabFragment();
            case 1:
                return new AppointmentsUpcomingTabFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
