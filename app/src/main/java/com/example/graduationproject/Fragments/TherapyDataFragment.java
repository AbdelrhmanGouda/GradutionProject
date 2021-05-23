package com.example.graduationproject.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.Data.TherapistsByNameData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class TherapyDataFragment extends Fragment {

    CircleImageView therapyProfileImage;
    TextView therapyNameTextView,therapyDescription, therapySessionCostTextView,
            therapyMobileNumberTextView, clinicLocationTextView;
    String therapyId,dayName=null;;
    FirebaseUser currentPatient;
    DatabaseReference patientNameRef, patientRefBook, therapyRef;

    AutoCompleteTextView autoCompleteTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_therapy_data, container, false);

        therapyProfileImage = view.findViewById(R.id.therapy_photo);
        therapyNameTextView = view.findViewById(R.id.therapy_name);
        therapySessionCostTextView = view.findViewById(R.id.therapy_session_cost);
        therapyMobileNumberTextView = view.findViewById(R.id.therapy_mobile);
        clinicLocationTextView = view.findViewById(R.id.therapy_clinic_location);


        // used for getting thee therapist data from the data base

        getTherapyData();


        return view;
    }

    // used for getting thee therapist data from the data base
    public void getTherapyData(){

        if (getArguments()!= null){
            therapyId = getArguments().getString("id");
        }
        therapyRef = FirebaseDatabase.getInstance().getReference("Doctors").child(therapyId);
        therapyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TherapistsByNameData model = snapshot.getValue(TherapistsByNameData.class);
                therapyNameTextView.setText("DR:"+model.getName());
                clinicLocationTextView.setText("location:"+model.getLocation());
                therapyMobileNumberTextView.setText("phone:"+model.getPhone());
                Picasso.get().load(model.getImageUrl()).into(therapyProfileImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}