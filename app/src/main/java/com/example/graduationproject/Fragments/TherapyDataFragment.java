package com.example.graduationproject.Fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.Adapter.TherapistsTimeDataAdapter;
import com.example.graduationproject.Data.TherapistsByNameData;
import com.example.graduationproject.Data.TherapistsReservationTimeData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class TherapyDataFragment extends Fragment {

    CircleImageView therapyProfileImage;
    TextView therapyNameTextView,therapyDescription, therapySessionCostTextView,
            therapyMobileNumberTextView, clinicLocationTextView,selectDayTextView;

    String therapyId,therapyName,patientId,dayName,patientName,startTime,endTime,timeName;
    FirebaseUser currentPatient;
    DatabaseReference patientNameRef, patientBookRef, therapyRef,timeBookRefForTherapy,savePatientRef,deleteSelectedTime;
    Button book;


    AlertDialog.Builder builder;
    RecyclerView recyclerView;
    List<TherapistsReservationTimeData> therapistsTimeList;
    LinearLayoutManager layoutManager;


    TherapistsTimeDataAdapter therapistsTimeAdapter;

    // Date picker dialog
    Calendar c = Calendar.getInstance();

     int year,month,day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_therapy_data, container, false);

        currentPatient = FirebaseAuth.getInstance().getCurrentUser();
        patientId = currentPatient.getUid();

        therapyProfileImage = view.findViewById(R.id.therapy_photo);
        therapyNameTextView = view.findViewById(R.id.therapy_name);
        therapySessionCostTextView = view.findViewById(R.id.therapy_session_cost);
        therapyMobileNumberTextView = view.findViewById(R.id.therapy_mobile);
        clinicLocationTextView = view.findViewById(R.id.therapy_clinic_location);
        selectDayTextView = view.findViewById(R.id.select_the_day);
        book = view.findViewById(R.id.therapy_book_button);
        builder = new AlertDialog.Builder(getActivity());
        book.setEnabled(true);

        layoutManager= new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = view.findViewById(R.id.appointments_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        therapistsTimeList = new ArrayList<>();

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

                getTimeName();

                builder.setTitle("Alert")
                        .setMessage("Are you sure for booking this time?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                confirmBooking();
                                Toast.makeText(getActivity(), "appointment is booked", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "you have not book any appointment ", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();



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
                getTime();

            }
        },year,month,day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();




    }

// get the time which the doctor have saved in the database
    private void getTime(){

        patientBookRef = FirebaseDatabase.getInstance().getReference("Doctors")
                    .child(therapyId).child("free time");

        if (dayName!=null){
            patientBookRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(dayName)){
                        patientBookRef.child(dayName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        TherapistsReservationTimeData model =
                                                dataSnapshot.getValue(TherapistsReservationTimeData.class);

                                        therapistsTimeList.add(model);

                                    }
                                therapistsTimeAdapter = new TherapistsTimeDataAdapter(getContext(),therapistsTimeList);
                                recyclerView.setAdapter(therapistsTimeAdapter);

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }else{
                        Toast.makeText(getActivity(), "the date which you have selected is not saved ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else{
            Toast.makeText(getActivity(), "select the date first", Toast.LENGTH_SHORT).show();
        }


    }
    // used for getting thee therapist data from the data base
    public void getTherapyData(){

        getPatientName();
        if (getArguments()!= null){
            therapyId = getArguments().getString("id");
            timeName = getArguments().getString("timeName");
        }
        therapyRef = FirebaseDatabase.getInstance().getReference("Doctors").child(therapyId);
        therapyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TherapistsByNameData model = snapshot.getValue(TherapistsByNameData.class);
                therapyName = model.getName();
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


    private void getTimeName(){

        // get the time name

        patientBookRef = FirebaseDatabase.getInstance().getReference("request appointment").child(patientId);
        patientBookRef.child("timeName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timeName = snapshot.getValue(String.class);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void confirmBooking(){

        if (dayName!=null){

            patientBookRef = FirebaseDatabase.getInstance().getReference("request appointment").child(patientId);
            patientBookRef.child("therapyId").setValue(therapyId);
            patientBookRef.child("therapyName").setValue(therapyName);
            patientBookRef.child("patientName").setValue(patientName);
            patientBookRef.child("dayDate").setValue(dayName);
            patientBookRef.child("requestStatus").setValue("confirmed");

            patientBookRef.child("startTime").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    startTime  = snapshot.getValue(String.class);
                    timeBookRefForTherapy.child(patientId).child("startTime").setValue(startTime);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            patientBookRef.child("endTime").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    endTime  = snapshot.getValue(String.class);
                    timeBookRefForTherapy.child(patientId).child("endTime").setValue(endTime);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // save the appointments to get them back
            timeBookRefForTherapy = FirebaseDatabase.getInstance().getReference("Doctors").child(therapyId)
                    .child("appointments").child(dayName);
            timeBookRefForTherapy.child(patientId)
                    .child("patientId").setValue(patientId);
            timeBookRefForTherapy.child(patientId).child("patientName").setValue(patientName);

            // save the patients to get them back for the doctor
            savePatientRef = FirebaseDatabase.getInstance().getReference("Doctors").child(therapyId)
                    .child("patients");
            savePatientRef.child(patientId).child("patientId").setValue(patientId);
            savePatientRef.child(patientId).child("patientName").setValue(patientName);



            if (timeName!=null) {
                deleteSelectedTime = FirebaseDatabase.getInstance().getReference("Doctors");
                deleteSelectedTime.child(therapyId).child("free time").child(dayName).child(timeName).removeValue();

            }else {
                Toast.makeText(getActivity(), "time name null ", Toast.LENGTH_SHORT).show();
            }


        }else {
            Toast.makeText(getActivity(), "select the date first", Toast.LENGTH_SHORT).show();

        }

    }

    private void getPatientName(){
        currentPatient = FirebaseAuth.getInstance().getCurrentUser();
        patientId = currentPatient.getUid();
        // get the patient name from the database
        patientNameRef = FirebaseDatabase.getInstance().getReference("Users");
        patientNameRef.child(patientId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientName = snapshot.getValue(String.class);
                Toast.makeText(getActivity(), patientName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void enableBooking(){

        book.setEnabled(false);
        patientBookRef = FirebaseDatabase.getInstance().getReference("request appointment").child(patientId);
        patientBookRef.child("patientId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id = snapshot.getValue(String.class);
                if (id==patientId){
                    book.setEnabled(true);
                    Toast.makeText(getActivity(), "id is correct", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "cannot book without choosing the time", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}