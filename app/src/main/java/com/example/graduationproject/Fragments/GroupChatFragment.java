package com.example.graduationproject.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Adapter.GroupChatMessagesAdapter;
import com.example.graduationproject.Data.GroupChat;
import com.example.graduationproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupChatFragment extends Fragment {
    String id;
    Toolbar toolbar;
    ImageButton send;
    ImageView groupIcon;
    TextView title;
    RecyclerView chatRecycler;
    FirebaseAuth firebaseAuth;
    ArrayList<GroupChat> groupChats;
    GroupChatMessagesAdapter groupChatMessagesAdapter;
    EditText groupMessage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_group_chat, container, false);
       groupMessage=view.findViewById(R.id.edit_group_message);
       title=view.findViewById(R.id.group_title);
       chatRecycler=view.findViewById(R.id.recycler_group_chat);
        toolbar=view.findViewById(R.id.toolbar);
        send=view.findViewById(R.id.btn_group_send);
        groupIcon=view.findViewById(R.id.group_icon);
        groupChats=new ArrayList<>();
        if(getArguments()!=null){
            id=getArguments().getString("id");

        }
        firebaseAuth=FirebaseAuth.getInstance();
        loadGroupInfo();
        showMessages();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=groupMessage.getText().toString();
                if(TextUtils.isEmpty(message)){
                    Toast.makeText(getActivity(), "cant send empty message..", Toast.LENGTH_SHORT).show();
                }else {
                    sendMessage(message);
                }
            }
        });

        return view;
    }

    private void showMessages() {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Groups");
        reference.child(id).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupChats.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    GroupChat groupChat=dataSnapshot.getValue(GroupChat.class);
                    groupChats.add(groupChat);
                }
                groupChatMessagesAdapter=new GroupChatMessagesAdapter(groupChats,getActivity());
                chatRecycler.setAdapter(groupChatMessagesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {
        String timestamp=""+System.currentTimeMillis();
        HashMap<String,String>  hashMap=new HashMap<>();
        hashMap.put("sender",firebaseAuth.getUid());
        hashMap.put("message",message);
        hashMap.put("timestamp",timestamp);
        hashMap.put("type","text"); //text or image or file
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Groups");
        reference.child(id).child("Messages").child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                groupMessage.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadGroupInfo() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Groups");
        reference.orderByChild("groupId").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String groupTitle=""+dataSnapshot.child("groupTitle").getValue();
                    String groupIcon1=""+dataSnapshot.child("groupIcon").getValue();
                    String createdBy=""+dataSnapshot.child("createdBy").getValue();
                    String timeStamp=""+dataSnapshot.child("timeStamp").getValue();
                    title.setText(groupTitle);
                    try {
                        Picasso.get().load(groupIcon1).into(groupIcon);

                    }catch (Exception e){
                            groupIcon.setImageResource(R.drawable.profile_image);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
