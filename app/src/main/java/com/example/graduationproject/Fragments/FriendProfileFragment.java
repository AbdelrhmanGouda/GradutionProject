
package com.example.graduationproject.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.graduationproject.Data.FriendListData;
/*import com.example.graduationproject.Notification.APIService;
import com.example.graduationproject.Notification.Client;
import com.example.graduationproject.Notification.Data;
import com.example.graduationproject.Notification.MyResponse;
import com.example.graduationproject.Notification.NotificationSender;
import com.example.graduationproject.Notification.Token;*/
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

/*
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
*/

public class FriendProfileFragment extends Fragment {
    private static final String TAG ="gouda" ;
    TextView name,loveNumbers,friendNumbers,location,bio;
    ImageView profile,addLove,addFriend;
    boolean flag=true,flag2=true;
    LinearLayout linearLove,linearAdd;
    String id,appreciateNumber,friendsNumber,uName,uri;
    FirebaseUser firebaseUser;
    String  userName,userImage,myName,myImage;
    DatabaseReference profileRefrence;

    Boolean loveState=true,addFriendState;
    int i=0,j=0,imageResourceId;
   /* private APIService apiService;
    String title ="Notification";
    String message = "Love";*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend_profile, container, false);
     //   ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        name=view.findViewById(R.id.profile_name);
        loveNumbers=view.findViewById(R.id.profile_love_text);
        friendNumbers=view.findViewById(R.id.profile_friend_text);
        location=view.findViewById(R.id.profile_location);
        bio=view.findViewById(R.id.profile_BIO);
        linearAdd=view.findViewById(R.id.linear_add);
        linearLove=view.findViewById(R.id.linear_love);
        profile=view.findViewById(R.id.profile_image_details);
        addLove=view.findViewById(R.id.profile_add_love);
        addFriend=view.findViewById(R.id.profile_add_friend);
        imageResourceId=R.drawable.whitelove;
       // apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        profileRefrence =FirebaseDatabase.getInstance().getReference("Profiles");

        if(getArguments()!=null){
            id=getArguments().getString("id");
            uName=getArguments().getString("name");
            uri=getArguments().getString("uri");

        }
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        getFriendData();
        setAddFriendImageState();
        setLoveImage();
        getApprciate();
        getFriendsNumber();

        linearLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==true){
                    addLove.setImageResource(R.drawable.bluelove);
                    setLoveImage();
                    makeLoves();
/*
                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(id).child("token")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String userToken=snapshot.getValue(String.class);
                                    sendNotifications(userToken,title,message);
                                    Log.i(TAG, "onDataChange: ");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
*/

                    flag=false;
                }else {
                    addLove.setImageResource(R.drawable.whitelove);
                    setLoveImage();
                    deleteLove();
                    flag=true;

                }
            }
        });
        linearAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag2==true){
                    addFriend.setImageResource(R.drawable.blueadd);
                   //addFriends();
                    sendFriendRequest();
                    setAddFriendImageState();
                    flag2=false;

                }else {
                    addFriend.setImageResource(R.drawable.whiteadd);
                   // deleteFriends();
                    deleteFirendRequest();
                    setAddFriendImageState();
                    flag2=true;

                }
            }
        });

        //UpdateToken();
        return view;
    }

  /*  private void UpdateToken() {
        FirebaseUser firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token =new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }*/

/*
    private void sendNotifications(String userToken, String title, String message) {
        Data data =new Data(title,message);
        NotificationSender notificationSender = new NotificationSender(data,userToken);
        Log.i(TAG, "sendNotifications: ");
        apiService.sendNotification(notificationSender).enqueue(new Callback<MyResponse>()
       {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code()==200){
                    if (response.body().success!=1){
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
*/


    private void getFriendData() {
        Query query6 = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                              userName=dataSnapshot.child("name").getValue(String.class);
                              userImage=dataSnapshot.child("uri").getValue(String.class);
                            String  userLocation=dataSnapshot.child("location").getValue(String.class);

                            name.setText(userName);
                            location.setText(userLocation);
                            Picasso.get().load(userImage).into(profile);

                        }

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Query query7 = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        query7.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // FriendListData user =snapshot.getValue(FriendListData.class);
                            myName=dataSnapshot.child("name").getValue(String.class);
                            myImage=dataSnapshot.child("uri").getValue(String.class);
                          //
                            //
                            //Toast.makeText(getActivity(), myName+"   "+myImage, Toast.LENGTH_SHORT).show();
                            String  userLocation=dataSnapshot.child("location").getValue(String.class);


                        }

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void makeLoves() {
        if(getApprciate()!=null){
            i=Integer.valueOf(getApprciate());
        }

        i++;
        loveNumbers.setText(String.valueOf(i));
        profileRefrence.child("Appreciate").child("Likes").child(id).child("Appreciate").setValue(String.valueOf(i));
        profileRefrence.child("Appreciate").child("AppreciateListNotification").child(id).child(firebaseUser.getUid()).child("name").setValue(myName);
        profileRefrence.child("Appreciate").child("AppreciateListNotification").child(id).child(firebaseUser.getUid()).child("uri").setValue(myImage);

        profileRefrence.child("Appreciate").child("FriendsStats").child(firebaseUser.getUid()).child(id).child("State").setValue(true);


    }
    private void deleteLove() {

        if(getApprciate()!=null){
            i=Integer.valueOf(getApprciate());
        }
        i--;
        loveNumbers.setText(String.valueOf(i));

        profileRefrence.child("Appreciate").child("Likes").child(id).child("Appreciate").setValue(String.valueOf(i));
        profileRefrence.child("Appreciate").child("AppreciateListNotification").child(firebaseUser.getUid()).child(id).child("name").removeValue();

        profileRefrence.child("Appreciate").child("FriendsStats").child(firebaseUser.getUid()).child(id).child("State").removeValue();

    }
    private void setLoveImage(){

        Query query6 = FirebaseDatabase.getInstance().getReference().child("Profiles").child("Appreciate").child("FriendsStats").child(firebaseUser.getUid()).child(id);
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            loveState =dataSnapshot.child("State").getValue(Boolean.class);

                 if(loveState){
                       addLove.setImageResource(R.drawable.bluelove);

                   }else {
                       addLove.setImageResource(R.drawable.whitelove);
                   }

                        }

                    }


                }else {
                    Toast.makeText(getActivity(), "no data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendFriendRequest() {
        FriendListData data=new FriendListData(myImage,myName,firebaseUser.getUid(),true);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("FriendsRequest").child(id).child(firebaseUser.getUid()).setValue(data);

    }
    private void deleteFirendRequest() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("FriendsRequest").child(id).child(firebaseUser.getUid()).removeValue();

    }


    private void addFriends() {
        if(getApprciate()!=null){
            j=Integer.valueOf(getApprciate());
        }

        FriendListData data=new FriendListData(myImage,myName,firebaseUser.getUid(),true);
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




    private void setAddFriendImageState(){
        Query query6 = FirebaseDatabase.getInstance().getReference().child("Profiles").child("Friends").child("FriendsList")
                .child(firebaseUser.getUid());
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                FriendListData data=snapshot.getValue(FriendListData.class);
                                if(data.getId().equals(id)){
                                    if(data.isState()){
                                        addFriend.setImageResource(R.drawable.blueadd);

                                    }else {
                                        addFriend.setImageResource(R.drawable.whiteadd);
                                    }
                                }

                        }


                    }


                }else {
                    Toast.makeText(getActivity(), "no data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
                            friendNumbers.setText(appreciateNumber);

                        }

                    }


                }else {
                    Toast.makeText(getActivity(), "no data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return appreciateNumber;

    }
    private String getFriendsNumber(){
        Query query6 = FirebaseDatabase.getInstance().getReference().child("Profiles").child("Appreciate").child("Likes")
                .child(id);
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            friendsNumber=dataSnapshot.child("Appreciate").getValue(String.class);
                            loveNumbers.setText(friendsNumber);

                        }

                    }


                }else {
                    Toast.makeText(getActivity(), "no data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return friendsNumber;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }


    /*private void makeLoves() {
        //  databaseReference =FirebaseDatabase.getInstance().getReference("Profiles");
        i++;
        //    databaseReference.child(id).child("Appreciate").setValue(i);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(STATE,true);
        editor.apply();

    }
    private void deleteLove(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(STATE,false);
        editor.apply();

    }
    private void stateLovePhoto(){
        SharedPreferences sharedPreferences1=getActivity().getSharedPreferences(SHARED, Context.MODE_PRIVATE);

        this.loveState=sharedPreferences1.getBoolean(STATE,false);
        if(this.loveState){
            addLove.setImageResource(R.drawable.bluelove);
        }else {
            addLove.setImageResource(R.drawable.whitelove);

        }

    }*/
    
}
