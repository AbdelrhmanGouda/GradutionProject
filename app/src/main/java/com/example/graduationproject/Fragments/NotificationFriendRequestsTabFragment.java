package com.example.graduationproject.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationproject.Adapter.FriendRequestTabAdapter;
import com.example.graduationproject.Adapter.NotificationTabAdapter;
import com.example.graduationproject.Data.FriendRequestTabData;
import com.example.graduationproject.Data.NotificationTabData;
import com.example.graduationproject.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationFriendRequestsTabFragment extends Fragment {
    private RecyclerView friendRequestTabRecyclerView;
    private List<FriendRequestTabData> friendRequestTabDataList;
    private FriendRequestTabAdapter friendRequestTabAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_friend_requests_tab, container, false);
        friendRequestTabRecyclerView=view.findViewById(R.id.friend_request_tab_recycler_view);
        friendRequestTabRecyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        friendRequestTabRecyclerView.setHasFixedSize(true);
        friendRequestTabDataList =new ArrayList<>();
        friendRequestTabDataList.add(new FriendRequestTabData("Ahmed Osama"));
        friendRequestTabDataList.add(new FriendRequestTabData("Abdelrhman Gouda"));
        friendRequestTabDataList.add(new FriendRequestTabData("Mohamed Nasser"));
        friendRequestTabDataList.add(new FriendRequestTabData("Khalid Nabil"));
        friendRequestTabDataList.add(new FriendRequestTabData("Al monofy"));
        friendRequestTabAdapter =new FriendRequestTabAdapter(friendRequestTabDataList,getContext());
        friendRequestTabRecyclerView.setAdapter(friendRequestTabAdapter);
        return view;
    }
}