package com.example.graduationproject.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Data.TherapistsByNameData;
import com.example.graduationproject.Fragments.TherapyDataFragment;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TherapistsByNameAdapter extends RecyclerView.Adapter <TherapistsByNameAdapter.ViewHolder>{

    private Context mContext;
    List<TherapistsByNameData> therapistsData;
    FirebaseUser currentPatient;
    DatabaseReference patientNameRef, patientRefBook,doctorRef;
    String patientId,patientName;


    public TherapistsByNameAdapter(Context mContext, List<TherapistsByNameData> getData) {
        this.mContext = mContext;
        this.therapistsData = getData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.therapists_custom_item,parent,false);
        return new TherapistsByNameAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewHolder viewHolder = holder;
        final TherapistsByNameData model = therapistsData.get(position);
        viewHolder.therapyName.setText(model.getName());
        viewHolder.clinicLocation.setText(model.getLocation());
        Picasso.get().load(model.getImageUrl()).into(holder.therapyPhoto);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("id",model.getId());
                TherapyDataFragment fragment = new TherapyDataFragment();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return therapistsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView therapyPhoto;
        TextView therapyName,therapyDescription,sessionCost,clinicLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            therapyPhoto = itemView.findViewById(R.id.therapy_photo);
            therapyName = itemView.findViewById(R.id.therapy_name);
            therapyDescription = itemView.findViewById(R.id.therapy_description);
            sessionCost = itemView.findViewById(R.id.therapy_session_cost);
            clinicLocation = itemView.findViewById(R.id.therapy_clinic_location);



        }
    }
}
