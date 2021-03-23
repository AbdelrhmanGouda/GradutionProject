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
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.Data.GroupChatList;
import com.example.graduationproject.Fragments.ChatMessageFragment;
import com.example.graduationproject.Fragments.GroupChatFragment;
import com.example.graduationproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.GroupChatAdapterViewHolder> {
        List<GroupChatList> friendListDataList;
        Context context;
public GroupChatAdapter(List<GroupChatList> friendListDataList, Context context){
        this.friendListDataList =friendListDataList;
        this.context =context;
        }
@NonNull
@Override
public GroupChatAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.freind_list_row, parent, false);
        return new GroupChatAdapterViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull GroupChatAdapterViewHolder holder, int position) {
// holder.friendListImage.setImageBitmap(friendListDataList.get(position).getFriendListImage());
final GroupChatList user=friendListDataList.get(position);
        holder.sender.setText("");
    holder.message.setText("");
    holder.time.setText("");

    loadLastMessage(user,holder);
        holder.friendListName.setText(user.getGroupTitle());
        //Picasso.get().load(user.ge()).into(holder.friendListImage);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
       AppCompatActivity activity=(AppCompatActivity)v.getContext();
        GroupChatFragment groupChatFragment=new GroupChatFragment();
        Bundle bundle=new Bundle();
        bundle.putString("id",user.getGroupId() );
        //set Fragmentclass Arguments
        groupChatFragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container,groupChatFragment).addToBackStack("").commit();



        }
        });

        }

    private void loadLastMessage(GroupChatList user, final GroupChatAdapterViewHolder holder) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Groups");
        reference.child(user.getGroupId()).child("Messages").limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (final DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String message=""+dataSnapshot.child("message").getValue();
                            String sender=""+dataSnapshot.child("sender").getValue();
                            String timestamp=""+dataSnapshot.child("timestamp").getValue();

                            Calendar cal=Calendar.getInstance(Locale.ENGLISH);
                            cal.setTimeInMillis(Long.parseLong(timestamp));
                            String timeFormat= DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();
                            holder.message.setText(message);
                            holder.time.setText(timeFormat);
                          DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users");
                            reference1.orderByChild("id").equalTo(sender)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                                String name=""+dataSnapshot1.child("name").getValue();
                                                holder.sender.setText(name+" : ");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });



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

public class GroupChatAdapterViewHolder extends RecyclerView.ViewHolder {
    ImageView friendListImage;
    TextView friendListName,time,message,sender;
    public GroupChatAdapterViewHolder(@NonNull View itemView) {
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
