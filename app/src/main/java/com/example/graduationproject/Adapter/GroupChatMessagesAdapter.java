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

import com.example.graduationproject.Data.GroupChat;
import com.example.graduationproject.Data.GroupChatList;
import com.example.graduationproject.Fragments.GroupChatFragment;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GroupChatMessagesAdapter extends RecyclerView.Adapter<GroupChatMessagesAdapter.GroupChatMessagesAdapterViewHolder> {
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;


    List<GroupChat> friendListDataList;
    Context context;
    FirebaseAuth firebaseAuth;
    public GroupChatMessagesAdapter(List<GroupChat> friendListDataList, Context context){
        this.friendListDataList =friendListDataList;
        this.context =context;
        firebaseAuth=FirebaseAuth.getInstance();
    }
    @NonNull
    @Override
    public GroupChatMessagesAdapter.GroupChatMessagesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_groupchat_right, parent, false);
            return new GroupChatMessagesAdapter.GroupChatMessagesAdapterViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_groupchat_left, parent, false);
            return new GroupChatMessagesAdapter.GroupChatMessagesAdapterViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatMessagesAdapter.GroupChatMessagesAdapterViewHolder holder, int position) {
// holder.friendListImage.setImageBitmap(friendListDataList.get(position).getFriendListImage());
        final GroupChat user=friendListDataList.get(position);

        Calendar cal=Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(user.getTimestamp()));
        String timeFormat= DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();

        holder.message.setText(user.getMessage());
        holder.time.setText(timeFormat);
        setUserName(user,holder);
        //Picasso.get().load(user.ge()).into(holder.friendListImage);

    }

    private void setUserName(GroupChat user, final GroupChatMessagesAdapterViewHolder holder) {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("id").equalTo(user.getSender()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String name=""+dataSnapshot.child("name").getValue();
                    holder.name.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
       FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(friendListDataList.get(position).getSender().equals(firebaseAuth.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
        // return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return friendListDataList.size();
    }

    public class GroupChatMessagesAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView name,message,time;
        public GroupChatMessagesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            message=itemView.findViewById(R.id.txt_group_message);
            time=itemView.findViewById(R.id.txt_group_time);
            //  friendListId=itemView.findViewById(R.id.friends_list_name);


        }
    }

}
