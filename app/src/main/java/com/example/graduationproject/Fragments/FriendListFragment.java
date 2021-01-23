package com.example.graduationproject.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationproject.Adapter.FriendListAdapter;
import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.R;

import java.util.ArrayList;
import java.util.List;

public class FriendListFragment extends Fragment {
    private RecyclerView friendListRecyclerView;
    private List<FriendListData> friendListDataList;
    private FriendListAdapter friendListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        friendListRecyclerView =view.findViewById(R.id.friends_list_recycler_view);
        friendListRecyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        friendListRecyclerView.setHasFixedSize(true);
        friendListDataList =new ArrayList<>();
        friendListDataList.add(new FriendListData("Ahmed Ali"));
        friendListDataList.add(new FriendListData("Ahmed Ali"));
        friendListDataList.add(new FriendListData("Ahmed Ali"));
        friendListDataList.add(new FriendListData("Ahmed Ali"));
        friendListAdapter =new FriendListAdapter(friendListDataList,getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
        friendListRecyclerView.addItemDecoration(dividerItemDecoration);
        friendListRecyclerView.setAdapter(friendListAdapter);
        return view;
    }
}