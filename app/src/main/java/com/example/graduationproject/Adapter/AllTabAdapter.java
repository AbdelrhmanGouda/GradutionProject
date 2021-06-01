package com.example.graduationproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.AllTabData;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AllTabAdapter extends RecyclerView.Adapter<AllTabAdapter.AllTabAdapterViewHolder> {
    List<AllTabData> allTabDataList;
    Context context;
    public AllTabAdapter (List<AllTabData> allTabDataList ,Context context){
        this.allTabDataList=allTabDataList;
        this.context=context;
    }
    @NonNull
    @Override
    public AllTabAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_tab_row, parent, false);
        return new AllTabAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllTabAdapterViewHolder holder, final int position) {
        holder.date.setText(allTabDataList.get(position).getDayDate());
        holder.startTime.setText(allTabDataList.get(position).getStartTime());
        holder.docName.setText(allTabDataList.get(position).getTherapyName());
        holder.endTime.setText(allTabDataList.get(position).getEndTime());
        holder.state.setText(allTabDataList.get(position).getState());
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = FirebaseDatabase.getInstance().getReference().child("request appointment")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot != null) {
                            if (snapshot.exists() && snapshot.getChildrenCount() > 0 && snapshot.getValue().toString().length() > 0) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    snapshot.getRef().child("state").setValue("Canceled");
                                }

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return allTabDataList.size();
    }

    public class AllTabAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView date, startTime,endTime,docName,state;
        Button cancel;
        public AllTabAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.all_tab_data);
            startTime =itemView.findViewById(R.id.all_tab_start_time);
            endTime=itemView.findViewById(R.id.all_tab_end_time);
            state=itemView.findViewById(R.id.all_tab_state);
            docName=itemView.findViewById(R.id.all_tab_doctor_name);
            cancel=itemView.findViewById(R.id.appointment_cancel_btn);
        }
    }
}
