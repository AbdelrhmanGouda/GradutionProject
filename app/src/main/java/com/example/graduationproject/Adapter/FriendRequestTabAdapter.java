package com.example.graduationproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.Data.FriendRequestTabData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FriendRequestTabAdapter extends RecyclerView.Adapter<FriendRequestTabAdapter.FriendRequestTabAdapterViewHolder> {
    List<FriendRequestTabData> friendRequestTabDataList;
    Context context;
    String id,appreciateNumber,friendsNumber,uName,uri;
    FirebaseUser firebaseUser;
    DatabaseReference profileRefrence;

    int i=0,j=0,imageResourceId;

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

    private void addFriends() {
        if(getApprciate()!=null){
            j=Integer.valueOf(getApprciate());
        }

        FriendListData data1=new FriendListData(userImage,userName,id,true);
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



        j--;
        profileRefrence.child("Friends").child("FriendsNumber").child(id).child("Friends").setValue(String.valueOf(j));
        profileRefrence.child("Friends").child("FriendsList").child(id).child(firebaseUser.getUid()).removeValue() ;
        profileRefrence.child("Friends").child("FriendsList").child(firebaseUser.getUid()).child(id).removeValue();


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
