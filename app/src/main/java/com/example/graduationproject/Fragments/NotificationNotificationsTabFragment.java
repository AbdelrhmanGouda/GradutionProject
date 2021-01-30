package com.example.graduationproject.Fragments;

import android.app.Notification;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationproject.Adapter.FriendListAdapter;
import com.example.graduationproject.Adapter.NotificationTabAdapter;
import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.Data.NotificationTabData;
import com.example.graduationproject.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationNotificationsTabFragment extends Fragment {
    private RecyclerView notificationTabRecyclerView;
    private List<NotificationTabData> notificationTabDataList;
    private NotificationTabAdapter notificationTabAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_notification_notifications_tab, container, false);
        notificationTabRecyclerView=view.findViewById(R.id.notification_tab_recycler_view);
        notificationTabRecyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        notificationTabRecyclerView.setHasFixedSize(true);
        notificationTabDataList =new ArrayList<>();
        notificationTabDataList.add(new NotificationTabData("Mariam Alaa","Asked you for an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mai Hassan","Send you an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mariam Alaa","Asked you for an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mai Hassan","Send you an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mariam Alaa","Asked you for an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mai Hassan","Send you an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mariam Alaa","Asked you for an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mai Hassan","Send you an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mariam Alaa","Asked you for an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mai Hassan","Send you an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mariam Alaa","Asked you for an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mai Hassan","Send you an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mariam Alaa","Asked you for an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mai Hassan","Send you an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mariam Alaa","Asked you for an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mai Hassan","Send you an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mariam Alaa","Asked you for an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mai Hassan","Send you an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mariam Alaa","Asked you for an appreciate"));
        notificationTabDataList.add(new NotificationTabData("Mai Hassan","Send you an appreciate"));

        notificationTabAdapter =new NotificationTabAdapter(notificationTabDataList,getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
        notificationTabRecyclerView.addItemDecoration(dividerItemDecoration);
        notificationTabRecyclerView.setAdapter(notificationTabAdapter);
        return view;
    }
}