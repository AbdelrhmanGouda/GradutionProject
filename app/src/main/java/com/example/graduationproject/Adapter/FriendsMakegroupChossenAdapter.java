package com.example.graduationproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsMakegroupChossenAdapter extends RecyclerView.Adapter<FriendsMakegroupChossenAdapter.makeGroupViewHolder> {
    Context context;
    ArrayList<FriendListData> friendListData;
    DatabaseReference databaseReference;
    String id;

    public FriendsMakegroupChossenAdapter(Context context, ArrayList<FriendListData> friendListData) {
        this.context = context;
        this.friendListData = friendListData;
    }

    public FriendsMakegroupChossenAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public makeGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_chossen_row,parent,false);
        return new makeGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final makeGroupViewHolder holder, int position) {
        final FriendListData friendData= friendListData.get(position);
        Picasso.get().load(friendData.getUri()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                databaseReference= FirebaseDatabase.getInstance().getReference("CreateGroup");
                databaseReference.child(firebaseUser.getUid()).child(friendData.getId()).removeValue();
                friendListData.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();

            }

        });
    }

    @Override
    public int getItemCount() {
        return friendListData.size();
    }

    public class makeGroupViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        public makeGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_chossen);
        }
    }
}
