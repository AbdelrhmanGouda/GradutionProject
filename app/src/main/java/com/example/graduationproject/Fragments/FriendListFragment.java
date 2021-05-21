package com.example.graduationproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.Adapter.FriendListAdapter;
import com.example.graduationproject.Adapter.GroupChatAdapter;
import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.Data.GroupChatList;
import com.example.graduationproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendListFragment extends Fragment {
    private RecyclerView friendListRecyclerView,groupChatRecycler;
    private List<FriendListData> friendListDataList;
    private List<GroupChatList> groupChatLists;
    private FriendListAdapter friendListAdapter;
    private GroupChatAdapter groupChatAdapter;
    LinearLayout linearLayout;
    FloatingActionButton addGroup;
    FirebaseUser firebaseUser;
    String type;
    TextView textViewGroup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
       // ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        groupChatRecycler=view.findViewById(R.id.recycler_groupchat);
        friendListRecyclerView =view.findViewById(R.id.friends_list_recycler_view);
        textViewGroup=view.findViewById(R.id.text);
        linearLayout=view.findViewById(R.id.location_linear);
        friendListRecyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        friendListRecyclerView.setHasFixedSize(true);
        groupChatRecycler.setLayoutManager( new LinearLayoutManager(getContext()));
        groupChatRecycler.setHasFixedSize(true);

        addGroup=view.findViewById(R.id.flating_group_chat);
        friendListDataList =new ArrayList<>();
        groupChatLists=new ArrayList<>();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
           // readData();
            FriendsList();
            groupChatList();

        if(getArguments()!=null){
            type=getArguments().getString("find");

        }
        if(type!=null){
            if(type.equals("people")){
                setHasOptionsMenu(true);
                addGroup.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                groupChatRecycler.setVisibility(View.VISIBLE);
                textViewGroup.setVisibility(View.VISIBLE);
            }

        }
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // bundle.putString("id", user.getId());
                //set Fragmentclass Arguments
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new ChooseGroupMember()).addToBackStack("").commit();

            }
        });

        return view;
    }

    private void groupChatList() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupChatLists.clear();
            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                if(dataSnapshot.child("Participants").child(firebaseUser.getUid()).exists()){
                    GroupChatList groupChatList=dataSnapshot.getValue(GroupChatList.class);
                    groupChatLists.add(groupChatList);
                  //  Toast.makeText(getActivity(), "mwgod", Toast.LENGTH_SHORT).show();

                }
                }
                groupChatAdapter=new GroupChatAdapter(groupChatLists,getActivity());
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
                groupChatRecycler.addItemDecoration(dividerItemDecoration);

                groupChatRecycler.setAdapter(groupChatAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FriendsList() {

        Query query6 = FirebaseDatabase.getInstance().getReference().child("Profiles").child("Friends").child("FriendsList")
                .child(firebaseUser.getUid());
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            FriendListData data=snapshot.getValue(FriendListData.class);
                            friendListDataList.add(data);

                            }

                        friendListAdapter =new FriendListAdapter(friendListDataList,getContext());
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
                        friendListRecyclerView.addItemDecoration(dividerItemDecoration);
                        friendListRecyclerView.setAdapter(friendListAdapter);



                    }


                }else {
                    Toast.makeText(getActivity(), "no data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                searchUsers(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchUsers(String s) {

        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Query reference= FirebaseDatabase.getInstance().getReference("Users").orderByChild("name")
                .startAt(s).endAt(s+"\uf0ff");
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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