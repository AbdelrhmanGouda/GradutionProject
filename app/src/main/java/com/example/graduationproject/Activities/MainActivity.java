package com.example.graduationproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.graduationproject.Fragments.AppointmentsFragment;
import com.example.graduationproject.Fragments.CartFragment;
import com.example.graduationproject.Fragments.FriendListFragment;
import com.example.graduationproject.Fragments.HomeFragment;
import com.example.graduationproject.Fragments.NotificationFragment;
import com.example.graduationproject.Fragments.ProfileFragment;
import com.example.graduationproject.Fragments.TherapistsFragment;
import com.example.graduationproject.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public FirebaseAuth mAuth;

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


        navigationView=findViewById(R.id.navigation);
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
               // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                //Toast.makeText(getActivity(),"Logged out!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, com.example.graduationproject.Sign.MainActivity.class));
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}