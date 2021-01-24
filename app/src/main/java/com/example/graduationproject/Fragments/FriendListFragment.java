package com.example.graduationproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.graduationproject.Adapter.FriendListAdapter;
import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendListFragment extends Fragment {
    private RecyclerView friendListRecyclerView;
    private List<FriendListData> friendListDataList;
    private FriendListAdapter friendListAdapter;
    LinearLayout linearLayout;
    String type;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        friendListRecyclerView =view.findViewById(R.id.friends_list_recycler_view);
        linearLayout=view.findViewById(R.id.location_linear);
        friendListRecyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        friendListRecyclerView.setHasFixedSize(true);
        friendListDataList =new ArrayList<>();
            readData();

        setHasOptionsMenu(true);
        if(getArguments()!=null){
            type=getArguments().getString("find");

        }
        if(type!=null){
            if(type.equals("people")){

                linearLayout.setVisibility(View.VISIBLE);
            }

        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
         inflater.inflate(R.menu.research_menu,menu);

        MenuItem menuItem=menu.findItem(R.id.search_menu);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Find Friends");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //arrayAdapter.getfilter.filter(newtext)
                //get database friends and show it in recyclerview
              //  friendListAdapter.get
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void readData(){
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendListDataList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    FriendListData friendListData=dataSnapshot.getValue(FriendListData.class);
                    assert friendListData!=null;
                    if(!friendListData.getId().equals(firebaseUser.getUid())){
                        friendListDataList.add(friendListData);
                    }
                }
                friendListAdapter =new FriendListAdapter(friendListDataList,getContext());
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
                friendListRecyclerView.addItemDecoration(dividerItemDecoration);
                friendListRecyclerView.setAdapter(friendListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}