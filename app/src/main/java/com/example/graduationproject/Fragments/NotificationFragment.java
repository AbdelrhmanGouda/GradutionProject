package com.example.graduationproject.Fragments;

import android.app.Notification;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationproject.Adapter.NotificationPagerAdapter;
import com.example.graduationproject.Adapter.PagerAdapter;
import com.example.graduationproject.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class NotificationFragment extends Fragment {
    private TabLayout tabLayout;
    private TabItem notificationTab,friendRequestTab;
    private NotificationPagerAdapter notificationPagerAdapter;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        viewPager = view.findViewById(R.id.notification_view_pager);
        tabLayout = view.findViewById(R.id.notification_tab_layout);
        notificationTab = view.findViewById(R.id.notification_tab_item);
        friendRequestTab = view.findViewById(R.id.friend_request_tab_item);

        notificationPagerAdapter=new NotificationPagerAdapter(getFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(notificationPagerAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition()==0){
                    notificationPagerAdapter.notifyDataSetChanged();
                }else if (tab.getPosition()==1){
                    notificationPagerAdapter.notifyDataSetChanged();
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
        return view;
    }
}