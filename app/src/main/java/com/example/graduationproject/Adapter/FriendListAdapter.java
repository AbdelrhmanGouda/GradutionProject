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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.Fragments.ChatMessageFragment;
import com.example.graduationproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        holder.friendListName.setText(user.getName());
        Picasso.get().load(user.getUri()).into(holder.friendListImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity=(AppCompatActivity)v.getContext();
                ChatMessageFragment messageFragment=new ChatMessageFragment();
                Bundle bundle=new Bundle();
                bundle.putString("id", user.getId());
                //set Fragmentclass Arguments
                messageFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,messageFragment).addToBackStack("").commit();



            }
        });

    }

    @Override
    public int getItemCount() {
        return friendListDataList.size();
    }

    public class FriendListAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView friendListImage;
        TextView friendListName;
        public FriendListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            friendListImage=itemView.findViewById(R.id.friends_list_image);
            friendListName=itemView.findViewById(R.id.friends_list_name);
           //  friendListId=itemView.findViewById(R.id.friends_list_name);

        }
    }
}
