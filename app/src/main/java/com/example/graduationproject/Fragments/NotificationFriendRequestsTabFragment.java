package com.example.graduationproject.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Adapter.FriendRequestTabAdapter;
import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationFriendRequestsTabFragment extends Fragment {
    private RecyclerView friendRequestTabRecyclerView;
    private List<FriendListData> friendListDataList;
    private FriendRequestTabAdapter friendRequestTabAdapter;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_friend_requests_tab, container, false);
        auth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=auth.getCurrentUser();
        String id =firebaseUser.getUid();
        friendRequestTabRecyclerView=view.findViewById(R.id.friend_request_tab_recycler_view);
        friendRequestTabRecyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        friendRequestTabRecyclerView.setHasFixedSize(true);
        friendListDataList =new ArrayList<>();


        Query query = FirebaseDatabase.getInstance().getReference().child("FriendsRequest").child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendListDataList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                      FriendListData friendListData = snapshot1.getValue(FriendListData.class);
                        friendListDataList.add(friendListData);
                        Log.i(friendListData.getName(), "list: ");


                    }
                    friendRequestTabAdapter =new FriendRequestTabAdapter(friendListDataList,getContext());
                    friendRequestTabRecyclerView.setAdapter(friendRequestTabAdapter);
                    friendRequestTabAdapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}