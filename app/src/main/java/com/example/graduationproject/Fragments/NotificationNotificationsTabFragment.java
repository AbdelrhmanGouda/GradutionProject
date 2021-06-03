package com.example.graduationproject.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Adapter.NotificationTabAdapter;
import com.example.graduationproject.Data.NotificationTabData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
        Query query = FirebaseDatabase.getInstance().getReference().child("Profiles").child("Appreciate").child("AppreciateListNotification")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationTabDataList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        NotificationTabData notificationTabData= snapshot1.getValue(NotificationTabData.class);
                        notificationTabDataList.add(notificationTabData);
                        Log.i(notificationTabData.getName(), "Notification sender: ");
                    }
                    notificationTabAdapter =new NotificationTabAdapter(notificationTabDataList,getContext());
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
                    notificationTabRecyclerView.addItemDecoration(dividerItemDecoration);
                    notificationTabRecyclerView.setAdapter(notificationTabAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}