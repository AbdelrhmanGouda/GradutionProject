package com.example.graduationproject.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.graduationproject.Fragments.TherapistsByLocationFragment;
import com.example.graduationproject.Fragments.TherapistsByNameFragment;

public class PagerAdapter  extends FragmentPagerAdapter {

    private int numOfTabs;

    public PagerAdapter(@NonNull FragmentManager fm,  int numberOfTabs) {
        super(fm);
        this.numOfTabs=numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TherapistsByNameFragment();
            case 1:
                return new TherapistsByLocationFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
