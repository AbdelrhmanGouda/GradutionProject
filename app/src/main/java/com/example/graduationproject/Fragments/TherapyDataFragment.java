package com.example.graduationproject.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class TherapyDataFragment extends Fragment {

    CircleImageView therapyProfileImage;
    TextView therapyNameTextView,therapyDescription, therapySessionCostTextView,
            therapyMobileNumberTextView, clinicLocationTextView,selectDayTextView;
    String therapyId,dayName;
    FirebaseUser currentPatient;
    DatabaseReference patientNameRef, patientRefBook, therapyRef;
    Button book;

    // Date picker dialog
    DatePickerDialog.OnDateSetListener onDateSetListener;
    Calendar c = Calendar.getInstance();

     int year,month,day;

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
        selectDayTextView = view.findViewById(R.id.select_the_day);
        book = view.findViewById(R.id.therapy_book_button);
        getTherapyData();

        // used for getting thee therapist data from the data base

        selectDayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showDatePickerDialog();

            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime();
            }
        });



        return view;
    }

    // show the date picker dialog to select the date of reservation
    private void showDatePickerDialog(){
        year= c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                SimpleDateFormat simpledateformat = new SimpleDateFormat("EE dd,MM,yy");
                c.set(year, month, dayOfMonth);
                String selectedDate = simpledateformat.format(c.getTime());
                selectDayTextView.setText(selectedDate);
                dayName = String.valueOf(selectedDate);
                Toast.makeText(getActivity(),dayName, Toast.LENGTH_SHORT).show();
            }
        },year,month,day);
        datePickerDialog.show();

    }

    private void getTime(){
        if (dayName=="wed"){

            patientRefBook = FirebaseDatabase.getInstance().getReference("Doctors")
                    .child(therapyId).child("free time").child("firday");
            patientRefBook.setValue("dghgh");
        }else {
            Toast.makeText(getActivity(), "non", Toast.LENGTH_SHORT).show();
        }

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