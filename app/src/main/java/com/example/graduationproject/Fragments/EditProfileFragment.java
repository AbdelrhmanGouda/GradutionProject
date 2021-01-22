package com.example.graduationproject.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.graduationproject.R;

public class EditProfileFragment extends Fragment {
    private ImageView editImage;
    private EditText editName, editEmail, editPassword, confirmPassword;
    private Button update;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        editImage =view.findViewById(R.id.edit_profile_image);
        editName =view.findViewById(R.id.edit_name);
        editEmail =view.findViewById(R.id.edit_email);
        editPassword =view.findViewById(R.id.edit_password);
        confirmPassword =view.findViewById(R.id.edit_confirm_password);
        update =view.findViewById(R.id.edit_profile_update_btn);
        return view;
    }
}