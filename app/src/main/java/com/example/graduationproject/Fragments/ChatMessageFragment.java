package com.example.graduationproject.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Adapter.MessageAdapter;
import com.example.graduationproject.Data.Chat;
import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageFragment extends Fragment {
    String id,name,uri;
    RecyclerView recyclerView;
    ArrayList<Chat> chats;
    MessageAdapter messageAdapter;
    CircleImageView circleImageView;
    TextView userName;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    EditText textSend;
    ImageButton send;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_message, container, false);
     // ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        textSend=view.findViewById(R.id.Edit_text_send);
        send=view.findViewById(R.id.btn_send);

        recyclerView=view.findViewById(R.id.recycler);
        //recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
      recyclerView.setLayoutManager(linearLayoutManager);

        Toolbar toolbar=view.findViewById(R.id.toolbar);
      final   AppCompatActivity activity=(AppCompatActivity)view.getContext();
        activity.setSupportActionBar(toolbar);
       // ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        activity.getSupportActionBar().setTitle("");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getFragmentManager().popBackStack();

            }
        });
        if(getArguments()!=null){
            id=getArguments().getString("id");
            name=getArguments().getString("name");
            uri=getArguments().getString("uri");

        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendProfileFragment profileFragment=new FriendProfileFragment();
                Bundle bundle=new Bundle();
                bundle.putString("id", id);
                bundle.putString("name", name);
                bundle.putString("uri", uri);

                //set Fragmentclass Arguments
                profileFragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,profileFragment).addToBackStack("").commit();

            }
        });

        circleImageView=view.findViewById(R.id.profile_image);
        userName=view.findViewById(R.id.username);
        getUser(id);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg= textSend.getText().toString();
                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),id,msg);
                }else {
                    Toast.makeText(getActivity(), "Cant send empty message!", Toast.LENGTH_SHORT).show();
                }
                textSend.setText("");

            }
        });
        return view;
    }
    private void getUser(final String id){
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
      //  databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(id);
        Query query6 = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                           // FriendListData user =snapshot.getValue(FriendListData.class);
                          String  name=dataSnapshot.child("name").getValue(String.class);
                            String  image=dataSnapshot.child("uri").getValue(String.class);

                            userName.setText(name);
                            Picasso.get().load(image).into(circleImageView);

                            readMessages(firebaseUser.getUid(), id,image);

                        }

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void sendMessage(String sender,String reciever,String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        Chat chat=new Chat(sender,reciever,message);
        reference.child("Chats").push().setValue(chat);

    }
    private void readMessages(final String myId, final String userId, final String uri){
    chats=new ArrayList<>();
    databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            chats.clear();
            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                Chat chat=dataSnapshot.getValue(Chat.class);
                if(chat.getSender().equals(myId)&&chat.getReciever().equals(userId)||
                   chat.getReciever().equals(myId)&&chat.getSender().equals(userId)){
                    chats.add(chat);
                }
                messageAdapter=new MessageAdapter(chats,getActivity(),uri);
                recyclerView.setAdapter(messageAdapter);

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

    }



}
