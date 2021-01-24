package com.example.graduationproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.Chat;
import com.example.graduationproject.Data.FriendListData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.FriendListAdapterViewHolder> {
    List<Chat> chats;
    String imageUri;
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    FirebaseUser firebaseUser;

    Context context;
    public MessageAdapter(List<Chat> chats, Context context,String imageUri){
        this.chats =chats;
        this.context =context;
        this.imageUri=imageUri;
    }
    @NonNull
    @Override
    public MessageAdapter.FriendListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     if(viewType==MSG_TYPE_RIGHT){
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right  , parent, false);
         return new MessageAdapter.FriendListAdapterViewHolder(view);
     }else {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left  , parent, false);
         return new MessageAdapter.FriendListAdapterViewHolder(view);

     }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.FriendListAdapterViewHolder holder, int position) {
        Chat chat=chats.get(position);
        holder.showMessage.setText(chat.getMessage());

        Picasso.get().load(imageUri).into(holder.friendListImage);

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }


    public class FriendListAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView friendListImage;
        TextView showMessage;
        public FriendListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            friendListImage=itemView.findViewById(R.id.chat_profile_image);
            showMessage=itemView.findViewById(R.id.show_message);

            //  friendListId=itemView.findViewById(R.id.friends_list_name);

        }
    }
    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
       // return super.getItemViewType(position);
    }

}
