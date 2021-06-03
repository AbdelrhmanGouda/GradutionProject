package com.example.graduationproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.NotificationTabData;
import com.example.graduationproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationTabAdapter extends RecyclerView.Adapter<NotificationTabAdapter.NotificationTabAdapterViewHolder> {
    List<NotificationTabData> notificationTabDataList;
    Context context;
    public NotificationTabAdapter(List<NotificationTabData> notificationTabDataList, Context context){
        this.notificationTabDataList =notificationTabDataList;
        this.context =context;
    }
    @NonNull
    @Override
    public NotificationTabAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_tab_row,parent,false);
        return new NotificationTabAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationTabAdapterViewHolder holder, int position) {
        holder.name.setText(notificationTabDataList.get(position).getName());
        String image=notificationTabDataList.get(position).getUri();
        Picasso.get().load(image).into(holder.uri);
    }

    @Override
    public int getItemCount() {
        return notificationTabDataList.size();
    }

    public class NotificationTabAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView uri;
        public NotificationTabAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.notification_tab_name);
            uri=itemView.findViewById(R.id.notification_tab_image);
        }
    }

}

