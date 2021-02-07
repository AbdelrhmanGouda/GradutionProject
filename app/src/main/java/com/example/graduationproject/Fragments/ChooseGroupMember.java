package com.example.graduationproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Adapter.FriendListAdapter;
import com.example.graduationproject.Adapter.FriendsMakegroupChossenAdapter;
import com.example.graduationproject.Adapter.ShowAllFriendsMakeGroupAdapter;
import com.example.graduationproject.Data.FriendListData;
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

public class ChooseGroupMember  extends Fragment {
    RecyclerView friendChossen,viewFriends;
    ShowAllFriendsMakeGroupAdapter makeGroupAdapter;
    FriendsMakegroupChossenAdapter chossenAdapter;
    ArrayList<FriendListData> listData;
    ArrayList<FriendListData> chossenData;

    FirebaseUser firebaseUser;
    String id;
    DatabaseReference reference;
    FloatingActionButton go;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_choose_groupmember,container,false);
        friendChossen=view.findViewById(R.id.recycler_member_chossen);
        viewFriends=view.findViewById(R.id.recycler_showFriends_makeGroup);
        go=view.findViewById(R.id.floating_right);
        listData=new ArrayList<>();
        chossenData=new ArrayList<>();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        getFriends();
        getChossenFriends();
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentCreateGroup createGroup=new FragmentCreateGroup();
                Bundle bundle=new Bundle();
                bundle.putParcelableArrayList("arraylist",chossenData);
                //set Fragmentclass Arguments
                createGroup.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,createGroup).addToBackStack("").commit();

            }
        });
        return view;

    }


    private void getFriends() {
        listData.clear();
        Query query6 = FirebaseDatabase.getInstance().getReference().child("Profiles").child("Friends").child("FriendsList")
                .child(firebaseUser.getUid());
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            FriendListData data=snapshot.getValue(FriendListData.class);
                            listData.add(data);

                        }
                        Toast.makeText(getActivity(), listData.get(0).getName(), Toast.LENGTH_SHORT).show();
                        makeGroupAdapter =new ShowAllFriendsMakeGroupAdapter(listData,getContext());

                        //LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
                        viewFriends.addItemDecoration(dividerItemDecoration);
                        viewFriends.setLayoutManager(new LinearLayoutManager(getContext()));
                        viewFriends.setAdapter(makeGroupAdapter);
                        makeGroupAdapter.notifyDataSetChanged();



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
    private void getChossenFriends() {
       // chossenData.clear();
        chossenAdapter =new FriendsMakegroupChossenAdapter(getContext(),chossenData);
        friendChossen.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        friendChossen.setAdapter(chossenAdapter);

        Query query6 = FirebaseDatabase.getInstance().getReference().child("CreateGroup").child(firebaseUser.getUid());

        query6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        chossenData.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            FriendListData data=snapshot.getValue(FriendListData.class);
                            chossenData.add(data);

                        }
                        if(chossenData!=null){
                            friendChossen.setVisibility(View.VISIBLE);
                        }
                       // Toast.makeText(getActivity(), firebaseUser.getUid(), Toast.LENGTH_SHORT).show();
                        chossenAdapter.notifyDataSetChanged();



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

}
