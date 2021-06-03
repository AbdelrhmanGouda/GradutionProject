package com.example.graduationproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowAllFriendsMakeGroupAdapter  extends RecyclerView.Adapter<ShowAllFriendsMakeGroupAdapter.FriendListAdapterViewHolder> {
    List<FriendListData> friendListDataList;
    Context context;
    boolean flag=true;
    FirebaseUser firebaseUser;
    String id;
    DatabaseReference databaseReference;
    FriendsMakegroupChossenAdapter chossenAdapter;
    public ShowAllFriendsMakeGroupAdapter(List<FriendListData> friendListDataList, Context context){
        this.friendListDataList =friendListDataList;
        this.context =context;
        chossenAdapter=new FriendsMakegroupChossenAdapter(context);
    }
    @NonNull
    @Override
    public FriendListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_members_row, parent, false);
        return new FriendListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendListAdapterViewHolder holder, int position) {
        //holder.friendListImage.setImageBitmap(friendListDataList.get(position).getFriendListImage());
        final FriendListData user=friendListDataList.get(position);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        id=firebaseUser.getUid();
        final FriendListData data=new FriendListData(user.getUri(),user.getName(),user.getId());

        holder.friendListName.setText(user.getName());
        Picasso.get().load(user.getUri()).into(holder.friendListImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag){
                    holder.chossen.setVisibility(View.VISIBLE);
                    databaseReference= FirebaseDatabase.getInstance().getReference("CreateGroup");
                    databaseReference.child(id).child(user.getId()).setValue(data);
                    flag=false;
                }else {
                    holder.chossen.setVisibility(View.GONE);
                    databaseReference= FirebaseDatabase.getInstance().getReference("CreateGroup");
                    databaseReference.child(id).child(user.getId()).removeValue();



                    flag=true;
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return friendListDataList.size();
    }

    public class FriendListAdapterViewHolder extends RecyclerView.ViewHolder {
        CircleImageView friendListImage,chossen;
        TextView friendListName;
        public FriendListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            friendListImage=itemView.findViewById(R.id.image);
            friendListName=itemView.findViewById(R.id.text_name);
            chossen=itemView.findViewById(R.id.correct_image);
            //  friendListId=itemView.findViewById(R.id.friends_list_name);

        }
    }
}
