package com.example.graduationproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Adapter.FriendsMakegroupChossenAdapter;
import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.R;

import java.util.ArrayList;

public class FragmentCreateGroup  extends Fragment {
    ArrayList<FriendListData> arraylist;
    RecyclerView groupMember;
    FriendsMakegroupChossenAdapter chossenAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_create_group,container,false);
       if(getArguments()!=null){
           arraylist= getArguments().getParcelableArrayList("arraylist");
       }
       groupMember=view.findViewById(R.id.recycler_group_member);
       chossenAdapter=new FriendsMakegroupChossenAdapter(getContext(),arraylist);
        groupMember.setLayoutManager(new GridLayoutManager(getContext(), 4));
        groupMember.setAdapter(chossenAdapter);


        return view;
    }
}
