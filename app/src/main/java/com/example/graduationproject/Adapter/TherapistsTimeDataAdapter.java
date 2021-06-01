package com.example.graduationproject.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.TherapistsReservationTimeData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TherapistsTimeDataAdapter extends RecyclerView.Adapter<TherapistsTimeDataAdapter.ViewHolder> {


    private Context mContext;
    List<TherapistsReservationTimeData> therapistsReservationTimeDataListData;
    DatabaseReference bookRef;
    FirebaseUser currentPatient;

    public TherapistsTimeDataAdapter(Context mContext, List<TherapistsReservationTimeData> therapistsReservationTimeDataListData) {
        this.mContext = mContext;
        this.therapistsReservationTimeDataListData = therapistsReservationTimeDataListData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.therapy_time_recycler_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final ViewHolder viewHolder = holder;
        final TherapistsReservationTimeData model = therapistsReservationTimeDataListData.get(position);
        viewHolder.startTimeTextView.setText(model.getStartTime());
        viewHolder.endTimeTextView.setText(model.getEndTime());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPatient = FirebaseAuth.getInstance().getCurrentUser();
                bookRef = FirebaseDatabase.getInstance().getReference()
                        .child("request appointment").child(currentPatient.getUid()).push();

                bookRef.child("startTime").setValue(model.getStartTime());
                bookRef.child("endTime").setValue(model.getEndTime());
                bookRef.child("timeName").setValue(model.getTimeName());
                bookRef.child("patientId").setValue(currentPatient.getUid());
                bookRef.child("requestStatus").setValue("not confirmed");
                bookRef.child("dayDate").setValue(model.getDayDate());


                Toast.makeText(mContext, "item is clicked", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return therapistsReservationTimeDataListData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView startTimeTextView,endTimeTextView,toTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);



            startTimeTextView = itemView.findViewById(R.id.start_time_text_view);
            toTextView = itemView.findViewById(R.id.to_text_view);
            endTimeTextView = itemView.findViewById(R.id.end_time_text_view);
        }
    }
}
