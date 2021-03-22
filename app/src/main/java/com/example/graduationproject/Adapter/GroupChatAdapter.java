package com.example.graduationproject.Adapter;

import android.content.Context;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import java.util.List;

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

@Override
public int getItemCount() {
        return friendListDataList.size();
        }

public class GroupChatAdapterViewHolder extends RecyclerView.ViewHolder {
    ImageView friendListImage;
    TextView friendListName;
    public GroupChatAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        friendListImage=itemView.findViewById(R.id.friends_list_image);
        friendListName=itemView.findViewById(R.id.friends_list_name);
        //  friendListId=itemView.findViewById(R.id.friends_list_name);


    }
}

}
