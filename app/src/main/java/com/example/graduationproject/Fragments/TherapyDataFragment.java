package com.example.graduationproject.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.Data.TherapistsByNameData;
import com.example.graduationproject.Data.TherapistsReservationTimeData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class TherapyDataFragment extends Fragment {

    CircleImageView therapyProfileImage;
    TextView therapyNameTextView,therapyDescription, therapySessionCostTextView,
            therapyMobileNumberTextView, clinicLocationTextView,selectDayTextView;
    TextView startTimeTextView1,endTimeTextView1;
    String therapyId,dayName;
    FirebaseUser currentPatient;
    DatabaseReference patientNameRef, patientBookRef, therapyRef;
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
        startTimeTextView1 = view.findViewById(R.id.start_time_text_view_card);
        endTimeTextView1 = view.findViewById(R.id.end_time_text_view_card);
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
                                    TherapistsReservationTimeData model =dataSnapshot.getValue(TherapistsReservationTimeData.class);
                                    startTimeTextView1.setVisibility(View.VISIBLE);
                                    startTimeTextView1.setText(model.getStartTime());
                                    endTimeTextView1.setText(model.getEndTime());
                                    Toast.makeText(getActivity(), model.getEndTime(), Toast.LENGTH_SHORT).show();
                                }
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