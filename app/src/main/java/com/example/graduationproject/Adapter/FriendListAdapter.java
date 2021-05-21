package com.example.graduationproject.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.Chat;
import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.Fragments.ChatMessageFragment;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListAdapterViewHolder> {
    List<FriendListData> friendListDataList;
    Context context;
    public FriendListAdapter(List<FriendListData> friendListDataList, Context context){
        this.friendListDataList =friendListDataList;
        this.context =context;
    }
    @NonNull
    @Override
    public FriendListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.freind_list_row, parent, false);
        return new FriendListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListAdapterViewHolder holder, int position) {
       // holder.friendListImage.setImageBitmap(friendListDataList.get(position).getFriendListImage());
        final FriendListData user=friendListDataList.get(position);
        holder.sender.setText("");
        holder.message.setText("");
        holder.time.setText("");

        viewLastMessage(user,holder);

        holder.friendListName.setText(user.getName());
        Picasso.get().load(user.getUri()).into(holder.friendListImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity=(AppCompatActivity)v.getContext();
                ChatMessageFragment messageFragment=new ChatMessageFragment();
                Bundle bundle=new Bundle();
                bundle.putString("id", user.getId());
                bundle.putString("name", user.getName());
                bundle.putString("uri", user.getUri());

                //set Fragmentclass Arguments
                messageFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,messageFragment).addToBackStack("").commit();



            }
        });

    }

    private void viewLastMessage(final FriendListData user, final FriendListAdapterViewHolder holder) {
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String message=dataSnapshot.child("message").getValue(String.class);
                    String reciever=dataSnapshot.child("reciever").getValue(String.class);
                    String sender=dataSnapshot.child("sender").getValue(String.class);

                    if(sender.equals(user.getId())&&reciever.equals(firebaseAuth.getUid())||
                            reciever.equals(user.getId())&&sender.equals(firebaseAuth.getUid())){
                       holder.message.setText(message);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return friendListDataList.size();
    }

    public class FriendListAdapterViewHolder extends RecyclerView.ViewHolder {
        CircleImageView friendListImage;
        TextView friendListName,time,message,sender;
        public FriendListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            friendListImage=itemView.findViewById(R.id.friends_list_image);
            friendListName=itemView.findViewById(R.id.friends_list_name);
            time=itemView.findViewById(R.id.friends_list_time);
            sender=itemView.findViewById(R.id.friends_list_sender);
            message=itemView.findViewById(R.id.friends_list_lastmessage);

            //  friendListId=itemView.findViewById(R.id.friends_list_name);

        }
    }
}
