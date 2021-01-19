package com.example.graduationproject.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.graduationproject.R;


public class HomeFragment extends Fragment {
    private CardView cardViewBot, cardViewFriend, cardViewTherapist ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cardViewBot = view.findViewById(R.id.home_chat_boot_card);
        cardViewFriend= view.findViewById(R.id.home_chat_person_card);
        cardViewTherapist=view.findViewById(R.id.home_therapist_card);

        cardViewBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Chat Bot", Toast.LENGTH_SHORT).show();
              //  FragmentManager fragmentManager = getFragmentManager();
              //  fragmentManager.beginTransaction().replace(R.id.fragment_container,).addToBackStack("").commit;
            }
        });

        cardViewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,new FriendListFragment()).addToBackStack("").commit();

            }
        });

        cardViewTherapist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,new TherapistsFragment()).addToBackStack("").commit();
            }
        });
        return view;
    }
}