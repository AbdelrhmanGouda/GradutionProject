package com.example.graduationproject.Fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentCreateGroup  extends Fragment {
    ArrayList<FriendListData> arraylist;
    RecyclerView groupMember;
    FloatingActionButton finish,takePhoto;
    EditText editGroupName;
    ProgressDialog dialog;
        FirebaseUser  firebaseUser;
    Uri image=null;
    FriendsMakegroupChossenAdapter chossenAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_create_group,container,false);
       if(getArguments()!=null){
           arraylist= getArguments().getParcelableArrayList("arraylist");
       }

           firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
       takePhoto=view.findViewById(R.id.take_group_photo);
       editGroupName=view.findViewById(R.id.edit_group_name);
       finish=view.findViewById(R.id.finish_group_chat);
       groupMember=view.findViewById(R.id.recycler_group_member);
       chossenAdapter=new FriendsMakegroupChossenAdapter(getContext(),arraylist);
        groupMember.setLayoutManager(new GridLayoutManager(getContext(), 4));
        groupMember.setAdapter(chossenAdapter);
    finish.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startCreateGroup();
        }
    });

        return view;
    }

    private void startCreateGroup() {
    dialog=new ProgressDialog(getActivity());
    dialog.setMessage("Creating Group");
    String title=editGroupName.getText().toString();
    String time=""+System.currentTimeMillis();
    if(TextUtils.isEmpty(title)){
        Toast.makeText(getActivity(), "enter group name ...", Toast.LENGTH_SHORT).show();
        return;
        }
    dialog.show();
    if(image==null){
        createGroup(time,title,"");
      }
    }


    private void createGroup(final String time, String groupName, String image) {
        final HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("groupId",time);
        hashMap.put("groupTitle",groupName);
        hashMap.put("groupIcon","");
        hashMap.put("createdBy",firebaseUser.getUid());
        hashMap.put("timeStamp",time);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Groups");
        reference.child(time).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                HashMap<String,String> hashMap1=new HashMap<>();

                hashMap1.put("uId",firebaseUser.getUid());
                hashMap1.put("role","creator");
                hashMap1.put("timeStamp",time);
                DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("Groups");
                reference1.child(time).child("Participants").child(firebaseUser.getUid()).setValue(hashMap1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Group Created ...", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
