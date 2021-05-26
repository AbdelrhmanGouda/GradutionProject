package com.example.graduationproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.Data.FriendRequestTabData;
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

import java.util.List;

public class FriendRequestTabAdapter extends RecyclerView.Adapter<FriendRequestTabAdapter.FriendRequestTabAdapterViewHolder> {
    List<FriendListData> friendListDataList;
    Context context;
    String id,appreciateNumber,userName,userImage,friendName,friendImage;
    DatabaseReference profileRefrence;
    FirebaseAuth auth;


    int i=0,j=0,imageResourceId;

    public FriendRequestTabAdapter(List<FriendListData> friendListDataList , Context context){
        this.friendListDataList =friendListDataList;
        this.context =context;
        auth=FirebaseAuth.getInstance();
        profileRefrence=FirebaseDatabase.getInstance().getReference();
        userData();

        //////

    }
    @NonNull
    @Override
    public FriendRequestTabAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_tab_row,parent,false);
        return new FriendRequestTabAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestTabAdapterViewHolder holder, final int position) {
        id =friendListDataList.get(position).getId();
        friendName=friendListDataList.get(position).getName();
        friendImage=friendListDataList.get(position).getUri();
        holder.name.setText(friendName);
        Picasso.get().load(friendImage).into(holder.imageView);
        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriends();
                deleteFirendRequest();
                friendListDataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeRemoved(position, getItemCount());
                Toast.makeText(context, "accepted", Toast.LENGTH_SHORT).show();
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFriends();
                deleteFirendRequest();
                friendListDataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeRemoved(position, getItemCount());
                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return friendListDataList.size();
    }

    public class FriendRequestTabAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageView;
        Button confirm,cancel;
        public FriendRequestTabAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.friend_request_tab_name);
            imageView=itemView.findViewById(R.id.friend_request_tab_image);
            confirm=itemView.findViewById(R.id.friend_request_tab_confirm_btn);
            cancel=itemView.findViewById(R.id.friend_request_tab_delete_btn);


        }
    }
    private void userData(){
        FirebaseUser firebaseUser=auth.getCurrentUser();
        Query query =FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!= null){
                    if (snapshot.exists()&& snapshot.getChildrenCount()>0&&snapshot.getValue().toString().length()>0){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            userName = snapshot.child("name").getValue(String.class);
                            userImage = snapshot.child("uri").getValue(String.class);
                            Log.i("username", userName);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

  private void addFriends() {
        if(getApprciate()!=null){
            j=Integer.valueOf(getApprciate());
        }
      FirebaseUser firebaseUser=auth.getCurrentUser();
      FriendListData data=new FriendListData(userImage,userName,firebaseUser.getUid(),true);
        FriendListData data1=new FriendListData(friendImage,friendName,id,true);
        j++;
        profileRefrence.child("Friends").child("FriendsNumber").child(id).child("Friends").setValue(String.valueOf(j));
        profileRefrence.child("Friends").child("FriendsNumber").child(firebaseUser.getUid()).child("Friends").setValue(String.valueOf(j));

        profileRefrence.child("Friends").child("FriendsList").child(id).child(firebaseUser.getUid()).setValue(data);
        profileRefrence.child("Friends").child("FriendsList").child(firebaseUser.getUid()).child(id).setValue(data1);

    }
    private void deleteFriends(){
        if(getApprciate()!=null){
            j=Integer.valueOf(getApprciate());
        }
       FirebaseUser  firebaseUser=auth.getCurrentUser();
        profileRefrence.child("Friends").child("FriendsList").child(id).child(firebaseUser.getUid()).removeValue() ;
        profileRefrence.child("Friends").child("FriendsList").child(firebaseUser.getUid()).child(id).removeValue();


    }
    private void deleteFirendRequest() {

        FirebaseUser  firebaseUser=auth.getCurrentUser();
        profileRefrence.child("FriendsRequest").child(firebaseUser.getUid()).child(id).removeValue();

    }
    private String getApprciate(){

        Query query6 = FirebaseDatabase.getInstance().getReference().child("Profiles").child("Friends").child("FriendsNumber")
                .child(id);
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            appreciateNumber=dataSnapshot.child("Friends").getValue(String.class);

                        }

                    }


                }else {
                    Toast.makeText(context, "no data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return appreciateNumber;

    }


}
