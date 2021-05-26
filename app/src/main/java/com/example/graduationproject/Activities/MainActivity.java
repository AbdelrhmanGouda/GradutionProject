package com.example.graduationproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.Fragments.AppointmentsFragment;
import com.example.graduationproject.Fragments.CartFragment;
import com.example.graduationproject.Fragments.FriendListFragment;
import com.example.graduationproject.Fragments.HomeFragment;
import com.example.graduationproject.Fragments.NotificationFragment;
import com.example.graduationproject.Fragments.ProfileFragment;
import com.example.graduationproject.Fragments.TherapistsFragment;
import com.example.graduationproject.R;
import com.example.graduationproject.Sign.SignMainActivity;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public FirebaseAuth mAuth;
    String userName,userImage;
    CircleImageView imgHead;
    View view;
    TextView textName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout =findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mAuth = FirebaseAuth.getInstance();
        getHeader();

        navigationView=findViewById(R.id.navigation);
        view =navigationView.getHeaderView(0);
        imgHead=view.findViewById(R.id.header_image);
        textName=view.findViewById(R.id.header_name);
        navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
                break;
            case R.id.friends_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FriendListFragment()).commit();
                break;
            case R.id.therapists:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TherapistsFragment()).commit();
                break;
            case R.id.notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NotificationFragment()).commit();
                break;
            case R.id.appointments:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AppointmentsFragment()).commit();
                break;
            case R.id.cart:
               getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CartFragment()).commit();
                break;
            case R.id.log_out:

                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();

                mAuth.signOut();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();

                Intent intent = new Intent(getApplicationContext(), SignMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //Toast.makeText(getActivity(),"Logged out!",Toast.LENGTH_LONG).show();

                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getHeader() {
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        final String id=firebaseUser.getUid();

        Query query6 = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0&&dataSnapshot.getValue().toString().length()>0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            userName=dataSnapshot.child("name").getValue(String.class);
                            userImage=dataSnapshot.child("uri").getValue(String.class);
                            Log.i(userName.toString(), "onDataChange: ");
                            textName.setText(userName);
                            Picasso.get().load(userImage).into(imgHead);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}