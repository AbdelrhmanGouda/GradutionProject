package com.example.graduationproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.FriendRequestTabData;
import com.example.graduationproject.R;

import java.util.List;

public class FriendRequestTabAdapter extends RecyclerView.Adapter<FriendRequestTabAdapter.FriendRequestTabAdapterViewHolder> {
    List<FriendRequestTabData> friendRequestTabDataList;
    Context context;
    public FriendRequestTabAdapter( List<FriendRequestTabData> friendRequestTabDataList ,Context context){
        this.friendRequestTabDataList =friendRequestTabDataList;
        this.context =context;
    }
    @NonNull
    @Override
    public FriendRequestTabAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_tab_row,parent,false);
        return new FriendRequestTabAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestTabAdapterViewHolder holder, int position) {
        holder.name.setText(friendRequestTabDataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return friendRequestTabDataList.size();
    }

    public class FriendRequestTabAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public FriendRequestTabAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.friend_request_tab_name);
        }
    }
}
