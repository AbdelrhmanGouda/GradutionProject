package com.example.graduationproject.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private TextView profileName,therapistNum,friendsNum,heartNum;
    private ImageView profilePic;
    private FloatingActionButton floatingActionButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        profileName=view.findViewById(R.id.profile_name);
        therapistNum=view.findViewById(R.id.profile_therapist_num);
        friendsNum=view.findViewById(R.id.profile_friends_num);
        heartNum=view.findViewById(R.id.profile_heart_num);
        profilePic=view.findViewById(R.id.profile_image);
        floatingActionButton=view.findViewById(R.id.floating_edit_profil);
        floatingActionButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,new EditProfileFragment()).addToBackStack("").commit();

    }
}